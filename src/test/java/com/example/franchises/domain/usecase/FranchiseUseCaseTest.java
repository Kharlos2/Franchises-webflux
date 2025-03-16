package com.example.franchises.domain.usecase;

import com.example.franchises.domain.models.Franchise;
import com.example.franchises.domain.models.StockBranchProduct;
import com.example.franchises.domain.spi.IFranchisePersistencePort;
import com.example.franchises.domain.usecase.validations.FranchiseValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    private IFranchisePersistencePort franchisePersistencePort;
    @Mock
    private FranchiseValidator franchiseValidator;
    @InjectMocks
    private FranchiseUseCase franchiseUseCase;



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
    @Test
    void findProductsStock_WhenFranchiseExists_ShouldReturnStock() {
        StockBranchProduct stockBranchProduct = new StockBranchProduct("Sucursal","Product A",10);

        when(franchiseValidator.validateExist(1L)).thenReturn(Mono.empty());
        when(franchisePersistencePort.findTopStockProductsByFranchiseId(anyLong())).thenReturn(Flux.just(stockBranchProduct));

        StepVerifier.create(franchiseUseCase.findProductsStock(1L))
                .expectNextMatches(stockBranchProduct1 ->
                        stockBranchProduct1.productName().equals("Product A"))
                .verifyComplete();
    }

}