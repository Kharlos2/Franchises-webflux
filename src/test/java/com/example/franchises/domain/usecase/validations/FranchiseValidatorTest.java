package com.example.franchises.domain.usecase.validations;


import com.example.franchises.domain.exceptions.BadRequestException;
import com.example.franchises.domain.exceptions.FranchiseAlreadyExistException;
import com.example.franchises.domain.models.Franchise;
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
class FranchiseValidatorTest {

    @Mock
    private IFranchisePersistencePort franchisePersistencePort;

    private FranchiseValidator franchiseValidator;

    @BeforeEach
    void setUp() {
        franchiseValidator = new FranchiseValidator(franchisePersistencePort);
    }

    @Test
    void validateName_ShouldPass_WhenNameIsValid() {
        String name = "Valid Name";

        StepVerifier.create(franchiseValidator.validateName(name))
                .verifyComplete();
    }

    @Test
    void validateName_ShouldFail_WhenNameIsNullOrBlank() {
        StepVerifier.create(franchiseValidator.validateName(null))
                .expectError(BadRequestException.class)
                .verify();

        StepVerifier.create(franchiseValidator.validateName(""))
                .expectError(BadRequestException.class)
                .verify();

        StepVerifier.create(franchiseValidator.validateName("   "))
                .expectError(BadRequestException.class)
                .verify();
    }

    @Test
    void validateNameExist_ShouldPass_WhenFranchiseDoesNotExist() {
        String franchiseName = "New Franchise";
        Mockito.when(franchisePersistencePort.findByName(franchiseName)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseValidator.validateNameExist(franchiseName))
                .verifyComplete();
    }

    @Test
    void validateNameExist_ShouldFail_WhenFranchiseAlreadyExists() {
        String franchiseName = "Existing Franchise";
        Mockito.when(franchisePersistencePort.findByName(franchiseName)).thenReturn(Mono.just(new Franchise()));

        StepVerifier.create(franchiseValidator.validateNameExist(franchiseName))
                .expectError(FranchiseAlreadyExistException.class)
                .verify();
    }


}