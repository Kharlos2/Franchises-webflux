package com.example.franchises.infrastructure.entrypoints;

import com.example.franchises.infrastructure.entrypoints.dtos.branch.BranchSaveDto;
import com.example.franchises.infrastructure.entrypoints.handlers.BranchHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BranchRouterRestTest {

    private WebTestClient webTestClient;
    private BranchHandler branchHandler;


    @BeforeEach
    void setUp() {
        branchHandler = Mockito.mock(BranchHandler.class);
        RouterRest branchRouter = new RouterRest();
        RouterFunction<ServerResponse> routerFunction = branchRouter.routerBranchFunction(branchHandler);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void createBranch_ShouldReturn201() {
        BranchSaveDto requestDto = new BranchSaveDto();
        requestDto.setName("Branch 1");
        requestDto.setFranchiseId(1L);

        when(branchHandler.save(any())).thenReturn(ServerResponse.created(null).build());

        webTestClient.post()
                .uri("/branch")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated();
    }
}