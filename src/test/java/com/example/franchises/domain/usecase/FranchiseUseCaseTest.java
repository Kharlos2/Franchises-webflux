package com.example.franchises.domain.usecase;

import com.example.franchises.domain.models.Franchise;
import com.example.franchises.domain.spi.IFranchisePersistencePort;
import com.example.franchises.domain.usecase.validations.FranchiseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    private IFranchisePersistencePort franchisePersistencePort;
    @Mock
    private FranchiseValidator franchiseValidator;

    private FranchiseUseCase franchiseUseCase;

    @BeforeEach
    void setUp() {
        franchiseUseCase = new FranchiseUseCase(franchisePersistencePort, franchiseValidator);
    }

    @Test
    void save_ShouldPass_WhenValidFranchise() {
        Franchise franchise = new Franchise(1L, "McDonald's");

        when(franchiseValidator.validateName(franchise.getName())).thenReturn(Mono.empty());
        when(franchiseValidator.validateNameExist(franchise.getName())).thenReturn(Mono.empty());
        when(franchisePersistencePort.save(franchise)).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseUseCase.save(franchise))
                .expectNext(franchise)
                .verifyComplete();
    }
}