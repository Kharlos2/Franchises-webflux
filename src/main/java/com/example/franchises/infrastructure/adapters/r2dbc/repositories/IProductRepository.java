package com.example.franchises.infrastructure.adapters.r2dbc.repositories;

import com.example.franchises.infrastructure.adapters.r2dbc.entities.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IProductRepository extends ReactiveCrudRepository<ProductEntity,Long> {

    @Query("SELECT * FROM public.products WHERE name ILIKE :name and branch_id = :branchId")
    Mono<ProductEntity> findByNameAndBranchId(String name, Long branchId);

}
