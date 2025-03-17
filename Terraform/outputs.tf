output "ecr_repository_url" {
  description = "ECR repository URL"
  value       = module.ecr.ecr_franchise_url
}

output "alb_dns_name" {
  description = "DNS of the Application Load Balancer"
  value       = module.alb.alb_dns_name
}
output "rds_secret_arn" {
  description = "Secret rds arn"
  value       = module.rds.rds_secret_arn
}
# output "db_endpoint" {
#   description = "RDS database endpoint"
#   value       = module.rds.db_endpoint
# }

# output "ecs_cluster_id" {
#   description = "ECS cluster ID"
#   value       = module.ecs.ecs_cluster_id
# }
