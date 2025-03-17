resource "aws_ecs_cluster" "franchise_cluster" {
  name = var.cluster_name
}

resource "aws_iam_role" "ecs_task_execution_role" {
  name = "ecsTaskExecutionRole-tf"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        },
        Action = "sts:AssumeRole"
      }
    ]
  })
}

resource "aws_iam_policy" "ecs_logging_and_ecr" {
  name        = "ECSLoggingAndECRPolicy"
  description = "Permite a ECS escribir logs en CloudWatch y descargar imágenes de ECR"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect   = "Allow",
        Action   = [
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents"
        ],
        Resource = "*"
      },
      {
        Effect   = "Allow",
        Action   = [
          "ecr:GetAuthorizationToken",
          "ecr:BatchCheckLayerAvailability",
          "ecr:GetDownloadUrlForLayer",
          "ecr:BatchGetImage"
        ],
        Resource = "*"
      },
      {
        Effect   = "Allow",
        Action   = [
          "secretsmanager:GetSecretValue",
          "secretsmanager:DescribeSecret"
        ],
        Resource = "*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_logging_and_ecr_attachment" {
  policy_arn = aws_iam_policy.ecs_logging_and_ecr.arn
  role       = aws_iam_role.ecs_task_execution_role.name
}

resource "aws_iam_policy_attachment" "ecs_execution_role_policy" {
  name       = "ecsExecutionRolePolicyTf"
  roles      = [aws_iam_role.ecs_task_execution_role.name]
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_security_group" "ecs_task_sg" {
  name        = "ecs-task-sg-tf"
  description = "Security Group para tareas ECS"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [var.alb-sg-tf]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_ecs_task_definition" "franchise_task" {
  family                   = "FranchiseTask-tf"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "512"
  memory                   = "1024"
  network_mode             = "awsvpc"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name  = "franchise-tf"
      image = "${var.repository_url}:latest"
      portMappings = [
        {
          containerPort = 8080
        }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = "/ecs/franchise"
          awslogs-region        = var.aws_region
          awslogs-stream-prefix = "ecs"
          awslogs-create-group  = "true"
        }
      }
      essential   = true
      environment = [
        {
          name  = "R2DBC_URL"
          value = "r2dbc:postgresql://${var.db_endpoint}/postgres?currentSchema=public&sslMode=require"
        }
      ]
      secrets = [
        {
          name  = "R2DBC_USERNAME"
          valueFrom = "${var.rds_secret_arn}:username::"
        },
        {
          name  = "R2DBC_PASSWORD"
          valueFrom = "${var.rds_secret_arn}:password::"
        }
      ]
    }
  ])
}

resource "aws_ecs_service" "franchise_service" {
  name            = "FranchiseServiceTf"
  cluster         = aws_ecs_cluster.franchise_cluster.id
  task_definition = aws_ecs_task_definition.franchise_task.arn
  launch_type     = "FARGATE"
  desired_count   = 1

  network_configuration {
    subnets         = var.subnet_ids
    security_groups = [aws_security_group.ecs_task_sg.id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = var.alb_target_group_arn
    container_name   = "franchise-tf"
    container_port   = 8080
  }

  depends_on = [
    aws_ecs_task_definition.franchise_task
  ]
}