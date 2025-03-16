package com.example.franchises.infrastructure.adapters.r2dbc.adapters;

import com.example.franchises.domain.models.Franchise;
import com.example.franchises.domain.models.StockBranchProduct;
import com.example.franchises.infrastructure.adapters.r2dbc.entities.FranchiseEntity;
import com.example.franchises.infrastructure.adapters.r2dbc.entities.StockBranchProductEntity;
import com.example.franchises.infrastructure.adapters.r2dbc.mappers.IFranchiseEntityMapper;
import com.example.franchises.infrastructure.adapters.r2dbc.repositories.IFranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranchisePersistenceAdapterTest {

    @Mock
    private IFranchiseRepository franchiseRepository;

    @Mock
    private IFranchiseEntityMapper franchiseEntityMapper;

    @InjectMocks
    private FranchisePersistenceAdapter franchisePersistenceAdapter;

    private Franchise franchise;
    private FranchiseEntity franchiseEntity;

    @BeforeEach
    void setUp() {
        franchise = new Franchise(1L, "McBurger");
        franchiseEntity = new FranchiseEntity(1L, "McBurger");
    }

    @Test
    void save_WhenFranchiseIsSaved_ShouldReturnFranchise() {
        when(franchiseEntityMapper.toFranchiseEntity(franchise)).thenReturn(franchiseEntity);
        when(franchiseRepository.save(franchiseEntity)).thenReturn(Mono.just(franchiseEntity));
        when(franchiseEntityMapper.toFranchiseModel(franchiseEntity)).thenReturn(franchise);

        StepVerifier.create(franchisePersistenceAdapter.save(franchise))
                .expectNext(franchise)
                .verifyComplete();

        verify(franchiseRepository, times(1)).save(franchiseEntity);
    }
    @Test
    void findById_WhenFranchiseExists_ShouldReturnFranchise() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchiseEntity));
        when(franchiseEntityMapper.toFranchiseModel(franchiseEntity)).thenReturn(franchise);

        StepVerifier.create(franchisePersistenceAdapter.findById(1L))
                .expectNext(franchise)
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(1L);
    }

    @Test
    void findByName_WhenFranchiseExists_ShouldReturnFranchise() {
        when(franchiseRepository.findByName("McBurger")).thenReturn(Mono.just(franchiseEntity));
        when(franchiseEntityMapper.toFranchiseModel(franchiseEntity)).thenReturn(franchise);

        StepVerifier.create(franchisePersistenceAdapter.findByName("McBurger"))
                .expectNext(franchise)
                .verifyComplete();

        verify(franchiseRepository, times(1)).findByName("McBurger");
    }
    @Test
    void findTopStockProductsByFranchiseId_WhenStockExists_ShouldReturnStockList() {
        StockBranchProductEntity stockBranchProduct = new StockBranchProductEntity("Sucursal A", "Product X", 20);
        StockBranchProduct stockBranchProduct1 = new StockBranchProduct("Sucursal A", "Product X", 20);
        when(franchiseRepository.findStockByFranchiseId(1L)).thenReturn(Flux.just(stockBranchProduct));
        when(franchiseEntityMapper.toStockModel(any())).thenReturn(stockBranchProduct1);
        StepVerifier.create(franchisePersistenceAdapter.findTopStockProductsByFranchiseId(1L))
                .expectNext(stockBranchProduct1)
                .verifyComplete();

        verify(franchiseRepository, times(1)).findStockByFranchiseId(1L);
    }

    @Test
    void findTopStockProductsByFranchiseId_WhenNoStockExists_ShouldReturnEmpty() {
        when(franchiseRepository.findStockByFranchiseId(1L)).thenReturn(Flux.empty());

        StepVerifier.create(franchisePersistenceAdapter.findTopStockProductsByFranchiseId(1L))
                .verifyComplete();

        verify(franchiseRepository, times(1)).findStockByFranchiseId(1L);
    }

}