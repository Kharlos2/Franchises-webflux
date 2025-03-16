package com.example.franchises.domain.usecase.validations;

import com.example.franchises.domain.exceptions.BadRequestException;
import com.example.franchises.domain.exceptions.BranchAlreadyExistException;
import com.example.franchises.domain.exceptions.BranchNotFoundException;
import com.example.franchises.domain.exceptions.FranchiseNotFoundException;
import com.example.franchises.domain.models.Branch;
import com.example.franchises.domain.models.Franchise;
import com.example.franchises.domain.spi.IBranchPersistencePort;
import com.example.franchises.domain.spi.IFranchisePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class BranchValidatorTest {
    @Mock
    private IBranchPersistencePort branchPersistencePort;

    @Mock
    private IFranchisePersistencePort franchisePersistencePort;

    private BranchValidator branchValidator;

    @BeforeEach
    void setUp() {
        branchValidator = new BranchValidator(branchPersistencePort, franchisePersistencePort);
    }

    @Test
    void validateFranchiseId_ShouldPass_WhenIdIsValid() {
        Long franchiseId = 1L;

        StepVerifier.create(branchValidator.validateFranchiseId(franchiseId))
                .verifyComplete();
    }

    @Test
    void validateFranchiseId_ShouldFail_WhenIdIsNull() {
        StepVerifier.create(branchValidator.validateFranchiseId(null))
                .expectError(BadRequestException.class)
                .verify();
    }

    @Test
    void validateFranchiseId_ShouldFail_WhenIdIsZeroOrNegative() {
        StepVerifier.create(branchValidator.validateFranchiseId(0L))
                .expectError(BadRequestException.class)
                .verify();

        StepVerifier.create(branchValidator.validateFranchiseId(-1L))
                .expectError(BadRequestException.class)
                .verify();
    }

    @Test
    void validateFranchiseExists_ShouldPass_WhenFranchiseExists() {
        Long franchiseId = 1L;
        Mockito.when(franchisePersistencePort.findById(franchiseId)).thenReturn(Mono.just(new Franchise()));

        StepVerifier.create(branchValidator.validateFranchiseExists(franchiseId))
                .verifyComplete();
    }

    @Test
    void validateFranchiseExists_ShouldFail_WhenFranchiseNotFound() {
        Long franchiseId = 1L;
        Mockito.when(franchisePersistencePort.findById(franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(branchValidator.validateFranchiseExists(franchiseId))
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }

    @Test
    void validateUniqueBranchName_ShouldPass_WhenBranchDoesNotExist() {
        String branchName = "NewBranch";
        Long franchiseId = 1L;
        Mockito.when(branchPersistencePort.findByNameAndFranchiseId(branchName, franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(branchValidator.validateUniqueBranchNameInFranchise(branchName, franchiseId))
                .verifyComplete();
    }

    @Test
    void validateUniqueBranchName_ShouldFail_WhenBranchAlreadyExists() {
        String branchName = "ExistingBranch";
        Long franchiseId = 1L;

        Mockito.when(branchPersistencePort.findByNameAndFranchiseId(branchName,franchiseId)).thenReturn(Mono.just(new Branch()));

        StepVerifier.create(branchValidator.validateUniqueBranchNameInFranchise(branchName,franchiseId))
                .expectError(BranchAlreadyExistException.class)
                .verify();
    }

    @Test
    void validateBranchName_ShouldPass_WhenNameIsValid() {
        String branchName = "Valid Name";

        StepVerifier.create(branchValidator.validateBranchName(branchName))
                .verifyComplete();
    }

    @Test
    void validateBranchName_ShouldFail_WhenNameIsNullOrBlank() {
        StepVerifier.create(branchValidator.validateBranchName(null))
                .expectError(BadRequestException.class)
                .verify();

        StepVerifier.create(branchValidator.validateBranchName(""))
                .expectError(BadRequestException.class)
                .verify();

        StepVerifier.create(branchValidator.validateBranchName("   "))
                .expectError(BadRequestException.class)
                .verify();
    }

    @Test
    void validateBranchExist_ShouldPass_WhenBranchExists() {
        Long branchId = 1L;
        Mockito.when(branchPersistencePort.findById(branchId)).thenReturn(Mono.just(new Branch()));

        StepVerifier.create(branchValidator.validateBranchExist(branchId))
                .verifyComplete();
    }

    @Test
    void validateBranchExist_ShouldFail_WhenBranchNotFound() {
        Long branchId = 1L;
        Mockito.when(branchPersistencePort.findById(branchId)).thenReturn(Mono.empty());

        StepVerifier.create(branchValidator.validateBranchExist(branchId))
                .expectError(BranchNotFoundException.class)
                .verify();
    }

}