# Variable Global
variable "vpc_id" {
  description = "Vpc id"
  type        = string
}

variable "cluster_name" {
  description = "Name of the ECS cluster"
  type        = string
}

variable "subnet_ids" {
  description = "List of subnet IDs for ECS tasks"
  type        = list(string)
}

variable "repository_url" {
  description = "ECR repository URL"
  type        = string
}

variable "db_endpoint" {
  description = "RDS database endpoint"
  type        = string
}

variable "alb_target_group_arn" {
  description = "ARN of the ALB target group"
  type        = string
}

variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "alb-sg-tf" {
  description = "Sg alb"
  type        = string
}

variable "rds_secret_arn" {
  description = "Rds secret arn"
  type        = string
}
