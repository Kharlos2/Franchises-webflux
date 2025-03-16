package com.example.franchises.infrastructure.adapters.r2dbc.repositories;

import com.example.franchises.infrastructure.adapters.r2dbc.entities.FranchiseEntity;
import com.example.franchises.infrastructure.adapters.r2dbc.entities.StockBranchProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IFranchiseRepository extends ReactiveCrudRepository<FranchiseEntity, Long> {

    @Query("SELECT * FROM public.franchises WHERE name ILIKE :name")
    Mono<FranchiseEntity> findByName(String franchiseName);

    @Query("""
             SELECT b.name AS branch_name, p.name AS product_name, p.stock
             FROM (
                 SELECT p.*,\s
                        RANK() OVER (PARTITION BY p.branch_id ORDER BY p.stock DESC) AS rank
                 FROM products p
             ) p
             JOIN branches b ON p.branch_id = b.id
             WHERE p.rank = 1 AND b.franchise_id = :franchiseId
    """)
    Flux<StockBranchProductEntity> findStockByFranchiseId(Long franchiseId);
}
