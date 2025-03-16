package com.example.franchises.infrastructure.entrypoints;


import com.example.franchises.infrastructure.entrypoints.handlers.FranchiseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.reactive.server.WebTestClient.bindToRouterFunction;

class FranchiseRouterRestTest {


    private WebTestClient webTestClient;
    private FranchiseHandler franchiseHandler;

    @BeforeEach
    void setUp() {
        franchiseHandler = mock(FranchiseHandler.class);

        RouterFunction<ServerResponse> routerFunction = new RouterRest().routerFranchiseFunction(franchiseHandler);
        webTestClient = bindToRouterFunction(routerFunction).build();
    }

    @Test
    void testSaveFranchise() {
        when(franchiseHandler.save(any())).thenReturn(ServerResponse.ok().bodyValue("Franchise Created"));

        webTestClient.post()
                .uri("/franchise")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Franchise Created");

        verify(franchiseHandler).save(any());
    }

    @Test
    void testHealthCheck() {
        when(franchiseHandler.healthCheck()).thenReturn(ServerResponse.ok().bodyValue("Healthy"));

        webTestClient.get()
                .uri("/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Healthy");

        verify(franchiseHandler).healthCheck();
    }

    @Test
    void testFindTopProducts() {
        when(franchiseHandler.findTopProducts(any())).thenReturn(ServerResponse.ok().bodyValue("Top Products List"));

        webTestClient.get()
                .uri("/franchise/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Top Products List");

        verify(franchiseHandler).findTopProducts(any());
    }
}