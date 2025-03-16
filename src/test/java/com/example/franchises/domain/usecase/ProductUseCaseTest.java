package com.example.franchises.domain.usecase;

import com.example.franchises.domain.models.Product;
import com.example.franchises.domain.spi.IProductPersistencePort;
import com.example.franchises.domain.usecase.validations.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {


    @Mock
    private IProductPersistencePort productPersistencePort;

    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    private ProductUseCase productUseCase;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Test Product", 1, 1L);
    }

    @Test
    void testSaveProduct() {
        when(productValidator.validateBranchId(anyLong())).thenReturn(Mono.empty());
        when(productValidator.validateBranch(anyLong())).thenReturn(Mono.empty());
        when(productValidator.validateEmptyName(anyString())).thenReturn(Mono.empty());
        when(productValidator.validateProductNameInBranch(anyString(), anyLong())).thenReturn(Mono.empty());
        when(productValidator.validateStock(anyInt())).thenReturn(Mono.empty());
        when(productPersistencePort.save(any(Product.class))).thenReturn(Mono.just(product));

        Mono<Product> result = productUseCase.save(product);

        StepVerifier.create(result)
                .expectNext(product)
                .verifyComplete();

        verify(productPersistencePort, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteRelationWithBranch() {
        when(productValidator.validateProductId(anyLong())).thenReturn(Mono.empty());
        when(productValidator.validateProductExists(anyLong())).thenReturn(Mono.just(product));
        when(productPersistencePort.save(any(Product.class))).thenReturn(Mono.just(product));

        Mono<Void> result = productUseCase.deleteRelationWithBranch(1L);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productPersistencePort, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateStock() {

        when(productValidator.validateProductExists(anyLong())).thenReturn(Mono.just(product));
        when(productValidator.validateStock(anyInt())).thenReturn(Mono.empty());
        when(productPersistencePort.save(any(Product.class))).thenReturn(Mono.just(product));

        Mono<Product> result = productUseCase.updateStock(1L, 2);

        StepVerifier.create(result)
                .expectNext(product)
                .verifyComplete();

        verify(productPersistencePort, times(1)).save(any(Product.class));
    }
}