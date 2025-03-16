package com.example.franchises.infrastructure.adapters.r2dbc.adapters;

import com.example.franchises.domain.models.Branch;
import com.example.franchises.infrastructure.adapters.r2dbc.entities.BranchEntity;
import com.example.franchises.infrastructure.adapters.r2dbc.mappers.IBranchEntityMapper;
import com.example.franchises.infrastructure.adapters.r2dbc.repositories.IBranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchPersistenceAdapterTest {
    @Mock
    private IBranchRepository branchRepository;

    @Mock
    private IBranchEntityMapper branchEntityMapper;

    @InjectMocks
    private BranchPersistenceAdapter branchPersistenceAdapter;

    private BranchEntity branchEntity;
    private Branch branch;

    @BeforeEach
    void setUp() {
        branch = new Branch(1L, "Branch 1", 1L);
        branchEntity = new BranchEntity(1L, "Branch 1", 1L);
    }


    @Test
    void save_WhenBranchIsSaved_ShouldReturnBranch() {

        when(branchEntityMapper.toBranchEntity(branch)).thenReturn(branchEntity);
        when(branchRepository.save(branchEntity)).thenReturn(Mono.just(branchEntity));
        when(branchEntityMapper.toBranchModel(branchEntity)).thenReturn(branch);

        StepVerifier.create(branchPersistenceAdapter.save(branch))
                .expectNextMatches(savedBranch -> savedBranch.getId().equals(1L))
                .verifyComplete();

        verify(branchRepository, times(1)).save(branchEntity);
    }
    @Test
    void findByName_WhenBranchExists_ShouldReturnBranch() {

        when(branchRepository.findByNameAndFranchiseId("Branch 1",1L)).thenReturn(Mono.just(branchEntity));
        when(branchEntityMapper.toBranchModel(branchEntity)).thenReturn(branch);

        StepVerifier.create(branchPersistenceAdapter.findByNameAndFranchiseId("Branch 1",1L))
                .expectNext(branch)
                .verifyComplete();

        verify(branchRepository, times(1)).findByNameAndFranchiseId("Branch 1",1L);
    }

    @Test
    void findByName_WhenBranchDoesNotExist_ShouldReturnEmptyMono() {
        when(branchRepository.findByNameAndFranchiseId("Nonexistent Branch",1L)).thenReturn(Mono.empty());

        StepVerifier.create(branchPersistenceAdapter.findByNameAndFranchiseId("Nonexistent Branch",1L))
                .verifyComplete();

        verify(branchRepository, times(1)).findByNameAndFranchiseId("Nonexistent Branch", 1L);
    }
}