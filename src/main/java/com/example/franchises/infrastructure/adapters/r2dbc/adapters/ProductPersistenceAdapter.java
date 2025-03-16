package com.example.franchises.infrastructure.adapters.r2dbc.adapters;

import com.example.franchises.domain.models.Product;
import com.example.franchises.domain.spi.IProductPersistencePort;
import com.example.franchises.infrastructure.adapters.r2dbc.mappers.IProductEntityMapper;
import com.example.franchises.infrastructure.adapters.r2dbc.repositories.IProductRepository;
import reactor.core.publisher.Mono;

public class ProductPersistenceAdapter implements IProductPersistencePort {

    private final IProductRepository productRepository;
    private final IProductEntityMapper productEntityMapper;

    public ProductPersistenceAdapter(IProductRepository productRepository, IProductEntityMapper productEntityMapper) {
        this.productRepository = productRepository;
        this.productEntityMapper = productEntityMapper;
    }

    @Override
    public Mono<Product> save(Product product) {
        return productRepository.save(productEntityMapper.toEntity(product))
                .map(productEntityMapper::toModel);
    }

    @Override
    public Mono<Product> findByNameAndBranchId(String name, Long branchId) {
        return productRepository.findByNameAndBranchId(name, branchId)
                .map(productEntityMapper::toModel);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return productRepository.findById(id)
                .map(productEntityMapper::toModel);
    }
}
