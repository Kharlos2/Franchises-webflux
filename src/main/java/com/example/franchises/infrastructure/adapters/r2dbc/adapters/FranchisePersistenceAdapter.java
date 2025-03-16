package com.example.franchises.infrastructure.adapters.r2dbc.adapters;

import com.example.franchises.domain.models.Franchise;
import com.example.franchises.domain.models.StockBranchProduct;
import com.example.franchises.domain.spi.IFranchisePersistencePort;
import com.example.franchises.infrastructure.adapters.r2dbc.entities.FranchiseEntity;
import com.example.franchises.infrastructure.adapters.r2dbc.mappers.IFranchiseEntityMapper;
import com.example.franchises.infrastructure.adapters.r2dbc.repositories.IFranchiseRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FranchisePersistenceAdapter implements IFranchisePersistencePort {
    private final IFranchiseRepository franchiseRepository;
    private final IFranchiseEntityMapper franchiseEntityMapper;

    public FranchisePersistenceAdapter(IFranchiseRepository franchiseRepository, IFranchiseEntityMapper franchiseEntityMapper) {
        this.franchiseRepository = franchiseRepository;
        this.franchiseEntityMapper = franchiseEntityMapper;
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        Mono<FranchiseEntity> franchiseEntityMono = franchiseRepository.save(franchiseEntityMapper.toFranchiseEntity(franchise));
        return franchiseEntityMono.map(franchiseEntityMapper::toFranchiseModel);
    }
    @Override
    public Mono<Franchise> findByName(String franchiseName) {
        return franchiseRepository.findByName(franchiseName).map(franchiseEntityMapper::toFranchiseModel);
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return franchiseRepository.findById(id).map(franchiseEntityMapper::toFranchiseModel);
    }

    @Override
    public Flux<StockBranchProduct> findTopStockProductsByFranchiseId(Long id) {
        return franchiseRepository.findStockByFranchiseId(id).map(franchiseEntityMapper::toStockModel);
    }
}
