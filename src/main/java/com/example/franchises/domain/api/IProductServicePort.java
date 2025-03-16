package com.example.franchises.domain.api;

import com.example.franchises.domain.models.Product;
import reactor.core.publisher.Mono;

public interface IProductServicePort {

    Mono<Product> save (Product product);
    Mono<Void> deleteRelationWithBranch(Long id);
    Mono<Product> updateStock(Long id, Integer stock);

}
