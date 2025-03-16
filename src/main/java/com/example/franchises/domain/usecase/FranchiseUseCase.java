package com.example.franchises.domain.usecase;

import com.example.franchises.domain.api.IFranchiseServicePort;
import com.example.franchises.domain.models.Franchise;
import com.example.franchises.domain.models.StockBranchProduct;
import com.example.franchises.domain.spi.IFranchisePersistencePort;
import com.example.franchises.domain.usecase.validations.FranchiseValidator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FranchiseUseCase implements IFranchiseServicePort {

    private final IFranchisePersistencePort franchisePersistencePort;
    private final FranchiseValidator franchiseValidator;

    public FranchiseUseCase(IFranchisePersistencePort franchisePersistencePort, FranchiseValidator franchiseValidator) {
        this.franchisePersistencePort = franchisePersistencePort;
        this.franchiseValidator = franchiseValidator;
    }


    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return Mono.when(
                franchiseValidator.validateName(franchise.getName()),
                franchiseValidator.validateNameExist(franchise.getName())
        ).then(
                franchisePersistencePort.save(franchise)
        );
    }

    @Override
    public Flux<StockBranchProduct> findProductsStock(Long franchiseId) {
        return Mono.when(
                        franchiseValidator.validateExist(franchiseId)
                )
                .thenMany(franchisePersistencePort.findTopStockProductsByFranchiseId(franchiseId));
    }

}
