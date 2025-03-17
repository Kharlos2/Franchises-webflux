output "ecr_franchise_url" {
  value = aws_ecr_repository.franchise_repo.repository_url                                          # The actual value to be outputted
  description = "Franchiser ECR URL"
}

output "ecr_franchise_name" {
  value = aws_ecr_repository.franchise_repo.name
  description = "Repository name"
}