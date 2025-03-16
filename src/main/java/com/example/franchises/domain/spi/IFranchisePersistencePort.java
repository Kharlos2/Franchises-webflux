package com.example.franchises.domain.spi;

import com.example.franchises.domain.models.Franchise;
import reactor.core.publisher.Mono;

public interface IFranchisePersistencePort {

    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findByName(String name);
    Mono<Franchise> findById(Long id);

}
