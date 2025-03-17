resource "aws_ecr_repository" "franchise_repo" {
  name = var.repository_name

  image_scanning_configuration {
    scan_on_push = true
  }
}
resource "aws_ecr_lifecycle_policy" "franchise_ecr_lifecycle" {
  policy = jsonencode({
    "rules": [
        {
            "rulePriority": 1,
            "description": "Expire images older than 14 days",
            "selection": {
                "tagStatus": "untagged",
                "countType": "sinceImagePushed",
                "countUnit": "days",
                "countNumber": 14
            },
            "action": {
                "type": "expire"
            }
        }
    ]
  })


  repository = aws_ecr_repository.franchise_repo.name
}