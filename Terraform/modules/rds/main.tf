resource "aws_security_group" "rds_sg" {
  name        = "rds-sg-tf"
  description = "Postgres access"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = var.allowed_cidr_blocks
  }

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    security_groups = [var.sg_bastion]
  }


  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = "web-sg"
  }
}

resource "aws_db_subnet_group" "rds_subnet_group_private" {
  name       = "rds-subnet-group-private"
  subnet_ids = var.subnet_ids_rds_private

  tags = {
    Name = "rds-subnet-group-private"
  }
}

resource "aws_db_instance" "rds_postgres" {
  identifier              = "franchise-tf"
  engine                  = "postgres"
  engine_version          = "17.2"
  instance_class          = "db.t4g.micro"
  allocated_storage       = 20
  publicly_accessible     = false
  vpc_security_group_ids  = [aws_security_group.rds_sg.id]
  db_subnet_group_name    = aws_db_subnet_group.rds_subnet_group_private.name
  username                = "postgres"
  manage_master_user_password = true
  multi_az                = false
  storage_type            = "gp2"
  backup_retention_period = 7
  skip_final_snapshot     = true
}
