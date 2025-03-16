package com.example.franchises.infrastructure.adapters.r2dbc.mappers;

import com.example.franchises.domain.models.Product;
import com.example.franchises.infrastructure.adapters.r2dbc.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IProductEntityMapper {

    ProductEntity toEntity (Product product);

    Product toModel(ProductEntity productEntity);

}
