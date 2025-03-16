package com.example.franchises.domain.usecase.validations;

import com.example.franchises.domain.exceptions.BadRequestException;
import com.example.franchises.domain.exceptions.BranchNotFoundException;
import com.example.franchises.domain.exceptions.ProductAlreadyExistException;
import com.example.franchises.domain.exceptions.ProductNotFoundException;
import com.example.franchises.domain.models.Branch;
import com.example.franchises.domain.models.Product;
import com.example.franchises.domain.spi.IBranchPersistencePort;
import com.example.franchises.domain.spi.IProductPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ProductValidatorTest {
    @Mock
    private IBranchPersistencePort branchPersistencePort;

    @Mock
    private IProductPersistencePort productPersistencePort;

    private ProductValidator productValidator;

    @BeforeEach
    void setUp() {
        productValidator = new ProductValidator(branchPersistencePort, productPersistencePort);
    }

    @Test
    void validateBranch_ShouldPass_WhenBranchExists() {
        Long branchId = 1L;
        Mockito.when(branchPersistencePort.findById(branchId)).thenReturn(Mono.just(new Branch()));

        StepVerifier.create(productValidator.validateBranch(branchId))
                .verifyComplete();
    }

    @Test
    void validateBranch_ShouldFail_WhenBranchNotFound() {
        Long branchId = 1L;
        Mockito.when(branchPersistencePort.findById(branchId)).thenReturn(Mono.empty());

        StepVerifier.create(productValidator.validateBranch(branchId))
                .expectError(BranchNotFoundException.class)
                .verify();
    }

    @Test
    void validateBranchId_ShouldPass_WhenBranchIdIsValid() {
        StepVerifier.create(productValidator.validateBranchId(1L))
                .verifyComplete();
    }

    @Test
    void validateBranchId_ShouldFail_WhenBranchIdIsNullOrInvalid() {
        StepVerifier.create(productValidator.validateBranchId(null))
                .expectError(BadRequestException.class)
                .verify();

        StepVerifier.create(productValidator.validateBranchId(0L))
                .expectError(BadRequestException.class)
                .verify();

        StepVerifier.create(productValidator.validateBranchId(-1L))
                .expectError(BadRequestException.class)
                .verify();
    }

    @Test
    void validateBranchId_ShouldPass_WhenProductIdIsValid() {
        StepVerifier.create(productValidator.validateProductId(1L))
                .verifyComplete();
    }

    @Test
    void validateBranchId_ShouldFail_WhenProductIdIsNullOrInvalid() {
        StepVerifier.create(productValidator.validateProductId(null))
                .expectError(BadRequestException.class)
                .verify();

        StepVerifier.create(productValidator.validateProductId(0L))
                .expectError(BadRequestException.class)
                .verify();

        StepVerifier.create(productValidator.validateProductId(-1L))
                .expectError(BadRequestException.class)
                .verify();
    }

    @Test
    void validateEmptyName_ShouldPass_WhenNameIsValid() {
        StepVerifier.create(productValidator.validateEmptyName("Product"))
                .verifyComplete();
    }

    @Test
    void validateEmptyName_ShouldFail_WhenNameIsNullOrBlank() {
        StepVerifier.create(productValidator.validateEmptyName(null))
                .expectError(BadRequestException.class)
                .verify();

        StepVerifier.create(productValidator.validateEmptyName(""))
                .expectError(BadRequestException.class)
                .verify();

        StepVerifier.create(productValidator.validateEmptyName("   "))
                .expectError(BadRequestException.class)
                .verify();
    }

    @Test
    void validateStock_ShouldPass_WhenStockIsValid() {
        StepVerifier.create(productValidator.validateStock(10))
                .verifyComplete();

        StepVerifier.create(productValidator.validateStock(0))
                .verifyComplete();
    }

    @Test
    void validateStock_ShouldFail_WhenStockIsNegative() {
        StepVerifier.create(productValidator.validateStock(-1))
                .expectError(BadRequestException.class)
                .verify();
    }

    @Test
    void validateProductName_ShouldPass_WhenProductDoesNotExist() {
        String productName = "New Product";
        Long branchId = 1L;
        Mockito.when(productPersistencePort.findByNameAndBranchId(productName, branchId)).thenReturn(Mono.empty());

        StepVerifier.create(productValidator.validateProductNameInBranch(productName, branchId))
                .verifyComplete();
    }

    @Test
    void validateProductName_ShouldFail_WhenProductAlreadyExists() {
        String productName = "Existing Product";
        Long branchId = 1L;
        Mockito.when(productPersistencePort.findByNameAndBranchId(productName, branchId)).thenReturn(Mono.just(new Product()));

        StepVerifier.create(productValidator.validateProductNameInBranch(productName, branchId))
                .expectError(ProductAlreadyExistException.class)
                .verify();
    }

    @Test
    void validateProductExist_successful(){
        Product product = new Product();
        Mockito.when(productPersistencePort.findById(anyLong())).thenReturn(Mono.just(product));

        StepVerifier.create(productValidator.validateProductExists(1L))
                .expectNext(product)
                .verifyComplete();
    }
    @Test
    void validateProductExist_shouldFail_ProductNotFoundException(){
        Mockito.when(productPersistencePort.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(productValidator.validateProductExists(1L))
                .expectError(ProductNotFoundException.class)
                .verify();
    }

}