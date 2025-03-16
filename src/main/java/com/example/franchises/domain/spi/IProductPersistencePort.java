package com.example.franchises.domain.spi;

import com.example.franchises.domain.models.Product;
import reactor.core.publisher.Mono;

public interface IProductPersistencePort {

    Mono<Product> save(Product product);
    Mono<Product> findByNameAndBranchId(String name, Long branchId);
}
