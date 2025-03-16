package com.example.franchises.domain.api;

import com.example.franchises.domain.models.Franchise;
import reactor.core.publisher.Mono;

public interface IFranchiseServicePort {

    Mono<Franchise> save(Franchise franchise);
}
