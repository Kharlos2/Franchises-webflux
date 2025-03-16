package com.example.franchises.infrastructure.adapters.r2dbc.repositories;

import com.example.franchises.infrastructure.adapters.r2dbc.entities.BranchEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IBranchRepository extends ReactiveCrudRepository<BranchEntity, Long> {

    @Query("SELECT * FROM public.branches WHERE name ILIKE :name and franchise_id = :franchiseId")
    Mono<BranchEntity> findByNameAndFranchiseId(String name, Long franchiseId);

}
