
#Variables Globales

variable "vpc_id" {
  description = "VPC id"
  type        = string
}

variable "aws_region" {
  type        = string
  default     = "us-east-1"
}

variable "profile" {
  type = string
}

# Variables modulos RDS
variable "subnet_ids_rds_private" {
  description = "Subnets private ids"
  type        = list(string)
}
variable "allowed_cidr_blocks" {
  description = "List cidr blocks"
  type        = list(string)
}

variable "sg_bastion" {
  description = "Security bastion ids"
  type        = string
}
# Variable modulo ALB
variable "subnet_ids_alb" {
  description = "Subnets ids"
  type        = list(string)
}
#Variable modulo ECS
variable "subnet_ids_ecs" {
  description = "Subnets ids"
  type        = list(string)
}