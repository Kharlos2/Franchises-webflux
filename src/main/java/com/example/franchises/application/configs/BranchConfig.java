package com.example.franchises.application.configs;

import com.example.franchises.domain.api.IBranchServicePort;
import com.example.franchises.domain.spi.IBranchPersistencePort;
import com.example.franchises.domain.spi.IFranchisePersistencePort;
import com.example.franchises.domain.usecase.BranchUseCase;
import com.example.franchises.domain.usecase.validations.BranchValidator;
import com.example.franchises.infrastructure.adapters.r2dbc.adapters.BranchPersistenceAdapter;
import com.example.franchises.infrastructure.adapters.r2dbc.mappers.IBranchEntityMapper;
import com.example.franchises.infrastructure.adapters.r2dbc.repositories.IBranchRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BranchConfig {

    private final IBranchRepository branchRepository;
    private final IBranchEntityMapper branchEntityMapper;

    public BranchConfig(IBranchRepository branchRepository, IBranchEntityMapper branchEntityMapper) {
        this.branchRepository = branchRepository;
        this.branchEntityMapper = branchEntityMapper;
    }

    @Bean
    public IBranchPersistencePort branchPersistencePort () {
        return new BranchPersistenceAdapter(branchRepository, branchEntityMapper);
    }
    @Bean
    public BranchValidator branchValidator(IBranchPersistencePort branchPersistencePort, IFranchisePersistencePort franchisePersistencePort){
        return new BranchValidator(branchPersistencePort, franchisePersistencePort);
    }

    @Bean
    public IBranchServicePort branchServicePort (
            IBranchPersistencePort branchPersistencePort,
            BranchValidator branchValidator
    ){
        return new BranchUseCase(branchPersistencePort,branchValidator);
    }

}
