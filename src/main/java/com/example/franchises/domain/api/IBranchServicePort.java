package com.example.franchises.domain.api;

import com.example.franchises.domain.models.Branch;
import reactor.core.publisher.Mono;

public interface IBranchServicePort {

    Mono<Branch> save(Branch branch);

}
