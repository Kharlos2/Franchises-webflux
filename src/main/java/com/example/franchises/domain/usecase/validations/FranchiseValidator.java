package com.example.franchises.domain.usecase.validations;

import com.example.franchises.domain.exceptions.BadRequestException;
import com.example.franchises.domain.exceptions.ExceptionsEnum;
import com.example.franchises.domain.exceptions.FranchiseAlreadyExistException;
import com.example.franchises.domain.spi.IFranchisePersistencePort;
import reactor.core.publisher.Mono;

public class FranchiseValidator {

    private final IFranchisePersistencePort franchisePersistencePort;

    public FranchiseValidator(IFranchisePersistencePort franchisePersistencePort) {
        this.franchisePersistencePort = franchisePersistencePort;
    }

    public Mono<Void> validateName(String name) {
        return Mono.justOrEmpty(name)
                .filter(n -> !n.isBlank())
                .switchIfEmpty(Mono.error(new BadRequestException(ExceptionsEnum.FRANCHISE_NAME_MANDATORY.getMessage())))
                .then();
    }
    public Mono<Void> validateNameExist(String name) {
        return franchisePersistencePort.findByName(name)
                .hasElement()
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.error(new FranchiseAlreadyExistException(ExceptionsEnum.ALREADY_EXIST_FRANCHISE.getMessage()))
                        : Mono.empty()
                );
    }

}
