package com.example.franchises.infrastructure.adapters.r2dbc.repositories;

import com.example.franchises.infrastructure.adapters.r2dbc.entities.FranchiseEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IFranchiseRepository extends ReactiveCrudRepository<FranchiseEntity, Long> {

    @Query("SELECT * FROM public.franchises WHERE name ILIKE :name")
    Mono<FranchiseEntity> findByName(String franchiseName);
}
