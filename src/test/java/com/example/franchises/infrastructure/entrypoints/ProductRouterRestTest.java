package com.example.franchises.infrastructure.entrypoints;

import com.example.franchises.infrastructure.entrypoints.handlers.ProductHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.reactive.server.WebTestClient.bindToRouterFunction;

class ProductRouterRestTest {

    private WebTestClient webTestClient;
    private ProductHandler productHandler;

    @BeforeEach
    void setUp() {
        productHandler = mock(ProductHandler.class);

        RouterFunction<ServerResponse> routerFunction = new RouterRest().routerProductFunction(productHandler);
        webTestClient = bindToRouterFunction(routerFunction).build();
    }

    @Test
    void testSaveProduct() {
        when(productHandler.save(any())).thenReturn(ServerResponse.ok().bodyValue("Product Created"));

        webTestClient.post()
                .uri("/product")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Product Created");

        verify(productHandler).save(any());
    }

    @Test
    void testDeleteProduct() {
        when(productHandler.delete(any())).thenReturn(ServerResponse.ok().bodyValue("Product Deleted"));

        webTestClient.delete()
                .uri("/product/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Product Deleted");

        verify(productHandler).delete(any());
    }

    @Test
    void testUpdateProductStock() {
        when(productHandler.updateStock(any())).thenReturn(ServerResponse.ok().bodyValue("Stock Updated"));

        webTestClient.patch()
                .uri("/product/update-stock/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Stock Updated");

        verify(productHandler).updateStock(any());
    }
    @Test
    void testUpdateProductName() {
        when(productHandler.updateName(any())).thenReturn(ServerResponse.ok().bodyValue("Product Name Updated"));

        webTestClient.patch()
                .uri("/product/1/name")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Product Name Updated");

        verify(productHandler).updateName(any());
    }

}