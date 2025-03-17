vpc_id      = "vpc-0a3a9576638d63562"
subnet_ids_alb  = ["subnet-05bbffa070db534c3","subnet-05d6b4f234d7faf6f"]
aws_region  = "us-east-1"
profile = "terraform"
#Variables rds
subnet_ids_rds_private= ["subnet-030199078807492fb","subnet-029e685e4604c0447"]
sg_bastion = "sg-0d825a844419275de"
allowed_cidr_blocks = ["10.0.144.0/20","10.0.128.0/20"]
#Variables de ECS
subnet_ids_ecs= ["subnet-030199078807492fb","subnet-029e685e4604c0447"]
