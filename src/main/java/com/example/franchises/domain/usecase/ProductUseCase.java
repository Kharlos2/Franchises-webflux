package com.example.franchises.domain.usecase;

import com.example.franchises.domain.api.IProductServicePort;
import com.example.franchises.domain.models.Product;
import com.example.franchises.domain.spi.IProductPersistencePort;
import com.example.franchises.domain.usecase.validations.ProductValidator;
import reactor.core.publisher.Mono;

public class ProductUseCase implements IProductServicePort {

    private final IProductPersistencePort productPersistencePort;
    private final ProductValidator productValidator;


    public ProductUseCase(IProductPersistencePort productPersistencePort, ProductValidator productValidator) {
        this.productPersistencePort = productPersistencePort;
        this.productValidator = productValidator;
    }

    @Override
    public Mono<Product> save(Product product) {
        return Mono.when(
                productValidator.validateBranchId(product.getBranchId()),
                productValidator.validateBranch(product.getBranchId()),
                productValidator.validateEmptyName(product.getName()),
                productValidator.validateProductNameInBranch(product.getName(), product.getBranchId()),
                productValidator.validateStock(product.getStock())
        ).then(productPersistencePort.save(product));
    }

    @Override
    public Mono<Void> deleteRelationWithBranch(Long id) {
        return Mono.when(
                productValidator.validateProductId(id)
        ).then(
                productValidator.validateProductExists(id)
                .flatMap(product -> productPersistencePort.save(new Product(product.getId(), product.getName(), product.getStock(), null)))
                .then()
        );
    }

    @Override
    public Mono<Product> updateStock(Long id, Integer stock) {
        return productValidator.validateStock(stock)
                .then(productValidator.validateProductExists(id))
                .flatMap(existingProduct -> {
                    existingProduct.setStock(stock);
                    return productPersistencePort.save(existingProduct);
                });
    }

    @Override
    public Mono<Product> updateName(Long id, String newName) {
        return productValidator.validateEmptyName(newName)
                .then(productValidator.validateProductExists(id))
                .flatMap(existingProduct ->
                        productValidator.validateProductNameInBranch(newName, existingProduct.getBranchId())
                                .then(Mono.defer(() -> {
                                    existingProduct.setName(newName);
                                    return productPersistencePort.save(existingProduct);
                                }))
                );
    }


}
