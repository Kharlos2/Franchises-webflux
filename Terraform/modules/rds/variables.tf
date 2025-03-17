variable "vpc_id" {
  description = "VPC id"
  type        = string
}

variable "subnet_ids_rds_private" {
  description = "Subnets privates IDs"
  type        = list(string)
}

variable "sg_bastion" {
  description = "Security private bastion IDs"
  type        = string
}

variable "allowed_cidr_blocks" {
  description = "List cidr blocks"
  type        = list(string)
}
