package com.example.franchises.domain.api;

import com.example.franchises.domain.models.Franchise;
import com.example.franchises.domain.models.StockBranchProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IFranchiseServicePort {

    Mono<Franchise> save(Franchise franchise);
    Flux<StockBranchProduct> findProductsStock(Long id);

}
