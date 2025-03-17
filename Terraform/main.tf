

module "ecr" {
  source = "./modules/ecr"
}

module "alb" {
  source     = "./modules/alb"
  vpc_id     = var.vpc_id
  subnet_ids = var.subnet_ids_alb
}

module "rds" {
  source      = "./modules/rds"
  vpc_id      = var.vpc_id
  subnet_ids_rds_private = var.subnet_ids_rds_private
  sg_bastion            = var.sg_bastion
  allowed_cidr_blocks   = var.allowed_cidr_blocks
}

module "ecs" {
  source               = "./modules/ecs"
  cluster_name         = "FranchiseCluster-tf"
  subnet_ids           = var.subnet_ids_ecs
  repository_url       = module.ecr.ecr_franchise_url
  db_endpoint          = module.rds.db_endpoint
  alb-sg-tf            = module.alb.sg_alb
  vpc_id               = var.vpc_id
  alb_target_group_arn = module.alb.target_group_arn
  aws_region           = var.aws_region
  rds_secret_arn       = module.rds.rds_secret_arn
}


