package com.example.franchises.domain.usecase.validations;

import com.example.franchises.domain.exceptions.BadRequestException;
import com.example.franchises.domain.exceptions.BranchNotFoundException;
import com.example.franchises.domain.exceptions.ExceptionsEnum;
import com.example.franchises.domain.exceptions.ProductAlreadyExistException;
import com.example.franchises.domain.spi.IBranchPersistencePort;
import com.example.franchises.domain.spi.IProductPersistencePort;
import reactor.core.publisher.Mono;

import static com.example.franchises.domain.utils.Constants.*;

public class ProductValidator {
    private final IBranchPersistencePort branchPersistencePort;
    private final IProductPersistencePort productPersistencePort;

    public ProductValidator(IBranchPersistencePort branchPersistencePort, IProductPersistencePort productPersistencePort) {
        this.branchPersistencePort = branchPersistencePort;
        this.productPersistencePort = productPersistencePort;
    }


    public Mono<Void> validateBranch(Long branchId) {
        return branchPersistencePort.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(ExceptionsEnum.BRANCH_NOT_FOUND.getMessage())))
                .then();
    }

    public Mono<Void> validateBranchId(Long branchId) {
        return Mono.justOrEmpty(branchId)
                .filter(id -> id != ZERO_NULL && id > ZERO_ID)
                .switchIfEmpty(Mono.error(new BadRequestException(ExceptionsEnum.BRANCH_ID_MANDATORY.getMessage())))
                .then();
    }

    public Mono<Void> validateEmptyName(String name) {
        return Mono.justOrEmpty(name)
                .filter(n -> !n.isBlank())
                .switchIfEmpty(Mono.error(new BadRequestException(ExceptionsEnum.EMPTY_PRODUCT_NAME.getMessage())))
                .then();
    }

    public Mono<Void> validateStock(Integer stock) {
        return Mono.justOrEmpty(stock)
                .filter(s -> s >= ZERO_STOCK)
                .switchIfEmpty(Mono.error(new BadRequestException(ExceptionsEnum.NEGATIVE_STOCK.getMessage())))
                .then();
    }

    public Mono<Void> validateProductNameInBranch(String name, Long branchId) {
        return productPersistencePort.findByNameAndBranchId(name, branchId)
                .hasElement()
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.error(new ProductAlreadyExistException(ExceptionsEnum.ALREADY_EXIST_PRODUCT.getMessage()))
                        : Mono.empty()
                );
    }



}
