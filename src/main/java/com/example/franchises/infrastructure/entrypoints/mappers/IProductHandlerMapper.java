package com.example.franchises.infrastructure.entrypoints.mappers;


import com.example.franchises.domain.models.Product;
import com.example.franchises.infrastructure.entrypoints.dtos.product.ProductResponseDto;
import com.example.franchises.infrastructure.entrypoints.dtos.product.ProductSaveDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IProductHandlerMapper {

    Product toModel (ProductSaveDto productSaveDto);
    ProductResponseDto toProductResponseDTO(Product product);
}
