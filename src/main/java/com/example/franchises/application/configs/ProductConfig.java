package com.example.franchises.application.configs;

import com.example.franchises.domain.api.IProductServicePort;
import com.example.franchises.domain.spi.IBranchPersistencePort;
import com.example.franchises.domain.spi.IProductPersistencePort;
import com.example.franchises.domain.usecase.ProductUseCase;
import com.example.franchises.domain.usecase.validations.ProductValidator;
import com.example.franchises.infrastructure.adapters.r2dbc.adapters.ProductPersistenceAdapter;
import com.example.franchises.infrastructure.adapters.r2dbc.mappers.IProductEntityMapper;
import com.example.franchises.infrastructure.adapters.r2dbc.repositories.IProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig {

    private final IProductRepository productRepository;
    private final IProductEntityMapper productEntityMapper;

    public ProductConfig(IProductRepository productRepository, IProductEntityMapper productEntityMapper) {
        this.productRepository = productRepository;
        this.productEntityMapper = productEntityMapper;
    }


    @Bean
    public IProductPersistencePort productPersistencePort(){
        return new ProductPersistenceAdapter(productRepository, productEntityMapper);
    }
    @Bean
    public ProductValidator productValidator(
            IBranchPersistencePort iBranchPersistencePort,
            IProductPersistencePort productPersistencePort
    ){
        return new ProductValidator(iBranchPersistencePort,productPersistencePort);
    }
    @Bean
    public IProductServicePort productServicePort(
            IProductPersistencePort productPersistencePort,
            ProductValidator productValidator
    ){
        return new ProductUseCase(productPersistencePort,productValidator);
    }
}
