package com.example.franchises.infrastructure.adapters.r2dbc.adapters;

import com.example.franchises.domain.models.Branch;
import com.example.franchises.domain.spi.IBranchPersistencePort;
import com.example.franchises.infrastructure.adapters.r2dbc.mappers.IBranchEntityMapper;
import com.example.franchises.infrastructure.adapters.r2dbc.repositories.IBranchRepository;
import reactor.core.publisher.Mono;

public class BranchPersistenceAdapter implements IBranchPersistencePort {

    private final IBranchRepository branchRepository;
    private final IBranchEntityMapper branchEntityMapper;

    public BranchPersistenceAdapter(IBranchRepository branchRepository, IBranchEntityMapper branchEntityMapper) {
        this.branchRepository = branchRepository;
        this.branchEntityMapper = branchEntityMapper;
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        return branchRepository.save(branchEntityMapper.toBranchEntity(branch))
                .map(branchEntityMapper::toBranchModel);
    }

    @Override
    public Mono<Branch> findByNameAndFranchiseId(String name, Long franchiseId) {
        return branchRepository.findByNameAndFranchiseId(name, franchiseId)
                .map(branchEntityMapper::toBranchModel);
    }

}
