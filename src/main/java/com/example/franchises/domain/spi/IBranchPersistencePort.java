package com.example.franchises.domain.spi;

import com.example.franchises.domain.models.Branch;
import reactor.core.publisher.Mono;

public interface IBranchPersistencePort {

    Mono<Branch> save(Branch branch);
    Mono<Branch> findByNameAndFranchiseId(String name, Long franchiseId);

}
