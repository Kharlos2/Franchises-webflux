package com.example.franchises.domain.usecase;

import com.example.franchises.domain.api.IBranchServicePort;
import com.example.franchises.domain.models.Branch;
import com.example.franchises.domain.spi.IBranchPersistencePort;
import com.example.franchises.domain.usecase.validations.BranchValidator;
import reactor.core.publisher.Mono;

public class BranchUseCase implements IBranchServicePort {

    private final IBranchPersistencePort branchPersistencePort;
    private final BranchValidator branchValidator;

    public BranchUseCase(IBranchPersistencePort branchPersistencePort, BranchValidator branchValidator) {
        this.branchPersistencePort = branchPersistencePort;
        this.branchValidator = branchValidator;
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        return Mono.when(
                branchValidator.validateFranchiseId(branch.getFranchiseId()),
                branchValidator.validateFranchiseExists(branch.getFranchiseId()),
                branchValidator.validateBranchName(branch.getName()),
                branchValidator.validateUniqueBranchNameInFranchise(branch.getName(), branch.getFranchiseId())
        ).then(
                branchPersistencePort.save(branch)
        );
    }

    @Override
    public Mono<Branch> updateName(Long id, String newName) {
        return Mono.when(
                branchValidator.validateBranchName(newName),
                branchValidator.validateBranchExist(id),
                branchValidator.validateUniqueBranchNameInFranchise(newName,id)
        ).then(
                branchPersistencePort.findById(id)
                        .flatMap(existingBranch -> {
                            existingBranch.setName(newName);
                            return branchPersistencePort.save(existingBranch);
                        })
        );
    }

}
