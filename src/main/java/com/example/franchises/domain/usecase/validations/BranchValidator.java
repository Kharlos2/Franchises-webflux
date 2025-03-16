package com.example.franchises.domain.usecase.validations;

import com.example.franchises.domain.exceptions.BadRequestException;
import com.example.franchises.domain.exceptions.BranchAlreadyExistException;
import com.example.franchises.domain.exceptions.ExceptionsEnum;
import com.example.franchises.domain.exceptions.FranchiseNotFoundException;
import com.example.franchises.domain.spi.IBranchPersistencePort;
import com.example.franchises.domain.spi.IFranchisePersistencePort;
import reactor.core.publisher.Mono;

import static com.example.franchises.domain.utils.Constants.ZERO_ID;
import static com.example.franchises.domain.utils.Constants.ZERO_NULL;

public class BranchValidator {

    private final IBranchPersistencePort branchPersistencePort;
    private final IFranchisePersistencePort franchisePersistencePort;

    public BranchValidator(IBranchPersistencePort branchPersistencePort, IFranchisePersistencePort franchisePersistencePort) {
        this.branchPersistencePort = branchPersistencePort;
        this.franchisePersistencePort = franchisePersistencePort;
    }

    public Mono<Void> validateFranchiseId(Long franchiseId) {
        return Mono.justOrEmpty(franchiseId)
                .filter(id -> id != ZERO_NULL && id > ZERO_ID)
                .switchIfEmpty(Mono.error(new BadRequestException(ExceptionsEnum.FRANCHISE_ID_REQUIRED.getMessage())))
                .then();
    }

    public Mono<Void> validateFranchiseExists(Long franchiseId) {
        return franchisePersistencePort.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(ExceptionsEnum.FRANCHISE_NOT_FOUND.getMessage())))
                .then();
    }

    public Mono<Void> validateUniqueBranchNameInFranchise(String name, Long franchiseId) {
        return branchPersistencePort.findByNameAndFranchiseId(name,franchiseId)
                .hasElement()
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.error(new BranchAlreadyExistException(ExceptionsEnum.ALREADY_EXIST_BRANCH.getMessage()))
                        : Mono.empty()
                );
    }

    public Mono<Void> validateBranchName(String newName) {
        return Mono.justOrEmpty(newName)
                .filter(n -> !n.isBlank())
                .switchIfEmpty(Mono.error(new BadRequestException(ExceptionsEnum.BRANCH_NAME_MANDATORY.getMessage())))
                .then();
    }

}
