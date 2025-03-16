package com.example.franchises.domain.usecase;

import com.example.franchises.domain.models.Branch;
import com.example.franchises.domain.spi.IBranchPersistencePort;
import com.example.franchises.domain.usecase.validations.BranchValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BranchUseCaseTest {

    @Mock
    IBranchPersistencePort branchPersistencePort;

    @Mock
    BranchValidator branchValidator;

    @InjectMocks
    BranchUseCase branchUseCase;

    Branch branch;

    @BeforeEach
    void setUp() {
        branch = new Branch();
        branch.setName("name");
        branch.setFranchiseId(1L);
        branch.setId(1L);
    }

    @Test
    void testSaveBranch() {
        when(branchPersistencePort.save(any(Branch.class))).thenReturn(Mono.empty());
        when(branchValidator.validateFranchiseId(anyLong())).thenReturn(Mono.empty());
        when(branchValidator.validateFranchiseExists(anyLong())).thenReturn(Mono.empty());
        when(branchValidator.validateBranchName(anyString())).thenReturn(Mono.empty());
        when(branchValidator.validateUniqueBranchNameInFranchise(anyString(),anyLong())).thenReturn(Mono.empty());

        Mono<Branch> result = branchUseCase.save(branch);

        StepVerifier.create(result)
                .verifyComplete();

        verify(branchPersistencePort, times(1)).save(any(Branch.class));
    }
    @Test
    void testUpdateBranchName() {
        when(branchPersistencePort.findById(anyLong())).thenReturn(Mono.just(branch));
        when(branchValidator.validateBranchExist(anyLong())).thenReturn(Mono.empty());
        when(branchValidator.validateBranchName(anyString())).thenReturn(Mono.empty());
        when(branchValidator.validateUniqueBranchNameInFranchise(anyString(),anyLong())).thenReturn(Mono.empty());
        when(branchPersistencePort.save(any(Branch.class))).thenReturn(Mono.empty());

        Mono<Branch> result = branchUseCase.updateName(1L, "New name");

        StepVerifier.create(result)
                .verifyComplete();

        verify(branchPersistencePort, times(1)).findById(anyLong());
        verify(branchPersistencePort, times(1)).save(any(Branch.class));
    }
}