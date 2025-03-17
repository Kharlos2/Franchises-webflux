output "alb_dns_name" {
  description = "DNS ALB"
  value       = aws_lb.franchise_alb.dns_name
}

output "target_group_arn" {
  description = "ARN target group"
  value       = aws_lb_target_group.target_group.arn
}

# output "ecs_task_sg_id" {
#   description = "ID security group for tasks"
#   value       = aws_security_group.ecs_task_sg.id
# }

output "sg_alb" {
  value = aws_security_group.alb_sg.id
  description = "Security group alb"
}