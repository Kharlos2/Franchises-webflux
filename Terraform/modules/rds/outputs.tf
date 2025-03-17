output "db_endpoint" {
  description = "RDS endpoint"
  value       = aws_db_instance.rds_postgres.endpoint
}

output "rds_secret_arn" {
  description = "secret arn"
  value       = aws_db_instance.rds_postgres.master_user_secret[0].secret_arn
}