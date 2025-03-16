package com.example.franchises.application.configs;

import com.example.franchises.domain.api.IFranchiseServicePort;
import com.example.franchises.domain.spi.IFranchisePersistencePort;
import com.example.franchises.domain.usecase.FranchiseUseCase;
import com.example.franchises.domain.usecase.validations.FranchiseValidator;
import com.example.franchises.infrastructure.adapters.r2dbc.adapters.FranchisePersistenceAdapter;
import com.example.franchises.infrastructure.adapters.r2dbc.mappers.IFranchiseEntityMapper;
import com.example.franchises.infrastructure.adapters.r2dbc.repositories.IFranchiseRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FranchiseConfig {

    private final IFranchiseRepository franchiseRepository;
    private final IFranchiseEntityMapper franchiseEntityMapper;

    public FranchiseConfig(IFranchiseRepository franchiseRepository, IFranchiseEntityMapper franchiseEntityMapper) {
        this.franchiseRepository = franchiseRepository;
        this.franchiseEntityMapper = franchiseEntityMapper;
    }

    @Bean
    public IFranchisePersistencePort franchisePersistencePort(){
        return new FranchisePersistenceAdapter(franchiseRepository, franchiseEntityMapper);
    }
    @Bean
    public FranchiseValidator franchiseValidator(IFranchisePersistencePort franchisePersistencePort){
        return new FranchiseValidator(franchisePersistencePort);
    }
    @Bean
    public IFranchiseServicePort iFranchiseServicePort(
            IFranchisePersistencePort franchisePersistencePort,
            FranchiseValidator franchiseValidator
    ){
        return new FranchiseUseCase(franchisePersistencePort, franchiseValidator);
    }

}
