package com.example.franchises.infrastructure.adapters.r2dbc.adapters;

import com.example.franchises.domain.models.Product;
import com.example.franchises.infrastructure.adapters.r2dbc.entities.ProductEntity;
import com.example.franchises.infrastructure.adapters.r2dbc.mappers.IProductEntityMapper;
import com.example.franchises.infrastructure.adapters.r2dbc.repositories.IProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductPersistenceAdapterTest {


    @Mock
    private IProductRepository productRepository;

    @Mock
    private IProductEntityMapper productEntityMapper;

    @InjectMocks
    private ProductPersistenceAdapter productPersistenceAdapter;

    private Product product;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Hamburguesa", 10, 5L);
        productEntity = new ProductEntity(1L, "Hamburguesa", 10, 1L);
    }

    @Test
    void save_WhenProductIsSaved_ShouldReturnProduct() {

        when(productEntityMapper.toEntity(product)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(Mono.just(productEntity));
        when(productEntityMapper.toModel(productEntity)).thenReturn(product);

        StepVerifier.create(productPersistenceAdapter.save(product))
                .expectNext(product)
                .verifyComplete();

        verify(productRepository, times(1)).save(productEntity);
    }

    @Test
    void findByName_WhenProductExists_ShouldReturnProduct() {
        when(productRepository.findByNameAndBranchId("Hamburguesa",1L)).thenReturn(Mono.just(productEntity));
        when(productEntityMapper.toModel(productEntity)).thenReturn(product);

        StepVerifier.create(productPersistenceAdapter.findByNameAndBranchId("Hamburguesa",1L))
                .expectNext(product)
                .verifyComplete();

        verify(productRepository, times(1)).findByNameAndBranchId("Hamburguesa",1L);
    }


}