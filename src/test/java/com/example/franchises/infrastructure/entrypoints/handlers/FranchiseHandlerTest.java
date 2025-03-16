package com.example.franchises.infrastructure.entrypoints.handlers;

import com.example.franchises.domain.api.IFranchiseServicePort;
import com.example.franchises.domain.exceptions.BadRequestException;
import com.example.franchises.domain.exceptions.FranchiseAlreadyExistException;
import com.example.franchises.domain.exceptions.FranchiseNotFoundException;
import com.example.franchises.domain.models.Franchise;
import com.example.franchises.domain.models.StockBranchProduct;
import com.example.franchises.infrastructure.entrypoints.dtos.UpdateNameDto;
import com.example.franchises.infrastructure.entrypoints.dtos.franchise.FranchiseResponseDto;
import com.example.franchises.infrastructure.entrypoints.dtos.franchise.FranchiseSaveDto;
import com.example.franchises.infrastructure.entrypoints.mappers.IFranchiseHandlerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FranchiseHandlerTest {


    @Mock
    private IFranchiseServicePort franchiseServicePort;

    @Mock
    private IFranchiseHandlerMapper franchiseHandlerMapper;

    @InjectMocks
    private FranchiseHandler franchiseHandler;

    private UpdateNameDto updateNameDto;
    @BeforeEach
    void setUp(){
        updateNameDto = new UpdateNameDto("Test Franchise");

    }


    @Test
    void testSaveSuccess() {
        FranchiseSaveDto dto = new FranchiseSaveDto("Test Franchise");
        FranchiseResponseDto responseDTO = new FranchiseResponseDto(1L, "Test Franchise");
        Franchise franchise = new Franchise(1L,"Test Franchise");
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/Franchise"))
                .body(Mono.just(dto));
        when(franchiseHandlerMapper.toModel(any())).thenReturn(franchise);
        when(franchiseServicePort.save(any())).thenReturn(Mono.just(franchise));
        when(franchiseHandlerMapper.toFranchiseResponseDTO(any())).thenReturn(responseDTO);


        StepVerifier.create(franchiseHandler.save(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.CREATED))
                .verifyComplete();

        verify(franchiseHandlerMapper, times(1)).toModel(dto);
        verify(franchiseServicePort, times(1)).save(franchise);


    }

    @Test
    void testSaveConflict() {
        FranchiseSaveDto dto = new FranchiseSaveDto("Test Franchise");
        Franchise franchise = new Franchise(1L,"Test Franchise");
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/Franchise"))
                .body(Mono.just(dto));
        when(franchiseHandlerMapper.toModel(any())).thenReturn(franchise);
        when(franchiseServicePort.save(any())).thenReturn(Mono.error(new FranchiseAlreadyExistException("Franchise already exist")));


        StepVerifier.create(franchiseHandler.save(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.CONFLICT))
                .verifyComplete();

        verify(franchiseServicePort, times(1)).save(franchise);

    }

    @Test
    void testSaveBadRequest() {
        FranchiseSaveDto dto = new FranchiseSaveDto("Test Franchise");
        Franchise franchise = new Franchise(1L,"Test Franchise");
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/Franchise"))
                .body(Mono.just(dto));
        when(franchiseHandlerMapper.toModel(any())).thenReturn(franchise);
        when(franchiseServicePort.save(any())).thenReturn(Mono.error(new BadRequestException("nO no no ")));


        StepVerifier.create(franchiseHandler.save(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();

        verify(franchiseServicePort, times(1)).save(franchise);
    }

    @Test
    void testSaveError() {
        FranchiseSaveDto dto = new FranchiseSaveDto("Test Franchise");
        Franchise franchise = new Franchise(1L,"Test Franchise");
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/Franchise"))
                .body(Mono.just(dto));
        when(franchiseHandlerMapper.toModel(any())).thenReturn(franchise);
        when(franchiseServicePort.save(any())).thenReturn(Mono.error(new RuntimeException("Error")));

        StepVerifier.create(franchiseHandler.save(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();

        verify(franchiseServicePort, times(1)).save(franchise);
    }
    @Test
    void healthCheck (){
        StepVerifier.create(franchiseHandler.healthCheck())
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void testFindTopProducts_Success() {
        Long franchiseId = 1L;
        List<StockBranchProduct> products = List.of(new StockBranchProduct("Branch A", "Product A", 10));
        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", String.valueOf(franchiseId))
                .build();

        when(franchiseServicePort.findProductsStock(franchiseId)).thenReturn(Flux.fromIterable(products));

        StepVerifier.create(franchiseHandler.findTopProducts(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(franchiseServicePort, times(1)).findProductsStock(franchiseId);
    }

    @Test
    void testFindTopProducts_NotFound() {
        Long franchiseId = 1L;
        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", String.valueOf(franchiseId))
                .build();

        when(franchiseServicePort.findProductsStock(franchiseId))
                .thenReturn(Flux.error(new FranchiseNotFoundException("Franchise not found")));

        StepVerifier.create(franchiseHandler.findTopProducts(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    void testFindTopProducts_InvalidFranchiseId() {
        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", "invalid")
                .build();

        StepVerifier.create(franchiseHandler.findTopProducts(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    @Test
    void testFindTopProducts_BadRequest() {
        Long franchiseId = 1L;
        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", String.valueOf(franchiseId))
                .build();

        when(franchiseServicePort.findProductsStock(franchiseId))
                .thenReturn(Flux.error(new BadRequestException("Invalid request")));

        StepVerifier.create(franchiseHandler.findTopProducts(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    @Test
    void testFindTopProducts_InternalServerError() {
        Long franchiseId = 1L;
        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", String.valueOf(franchiseId))
                .build();

        when(franchiseServicePort.findProductsStock(franchiseId))
                .thenReturn(Flux.error(new RuntimeException("Unexpected error")));

        StepVerifier.create(franchiseHandler.findTopProducts(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();
    }


    @Test
    void testUpdateSuccess() {
        FranchiseResponseDto responseDTO = new FranchiseResponseDto(1L, "Test Franchise");
        Franchise franchise = new Franchise(1L,"Test Franchise");
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/Franchise"))
                .pathVariable("id","1")
                .body(Mono.just(updateNameDto));
        when(franchiseServicePort.updateName(anyLong(),anyString())).thenReturn(Mono.just(franchise));
        when(franchiseHandlerMapper.toFranchiseResponseDTO(any())).thenReturn(responseDTO);


        StepVerifier.create(franchiseHandler.updateName(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(franchiseServicePort, times(1)).updateName(anyLong(),anyString());


    }

    @Test
    void testUpdateConflict() {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/Franchise"))
                .pathVariable("id","1")
                .body(Mono.just(updateNameDto));
        when(franchiseServicePort.updateName(anyLong(),anyString())).thenReturn(Mono.error(new FranchiseAlreadyExistException("Franchise already exist")));


        StepVerifier.create(franchiseHandler.updateName(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.CONFLICT))
                .verifyComplete();

        verify(franchiseServicePort, times(1)).updateName(anyLong(),anyString());

    }

    @Test
    void testUpdateBadRequest() {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/Franchise"))
                .pathVariable("id","1")
                .body(Mono.just(updateNameDto));
        when(franchiseServicePort.updateName(anyLong(),anyString())).thenReturn(Mono.error(new BadRequestException("nO no no ")));


        StepVerifier.create(franchiseHandler.updateName(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();

        verify(franchiseServicePort, times(1)).updateName(anyLong(),anyString());
    }

    @Test
    void testUpdateError() {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/Franchise"))
                .pathVariable("id","1")
                .body(Mono.just(updateNameDto));
        when(franchiseServicePort.updateName(anyLong(),anyString())).thenReturn(Mono.error(new RuntimeException("Error")));


        StepVerifier.create(franchiseHandler.updateName(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();

        verify(franchiseServicePort, times(1)).updateName(anyLong(),anyString());
    }


    @Test
    void testUpdateNotFound() {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/Franchise"))
                .pathVariable("id","1")
                .body(Mono.just(updateNameDto));
        when(franchiseServicePort.updateName(anyLong(),anyString())).thenReturn(Mono.error(new FranchiseNotFoundException("Error")));


        StepVerifier.create(franchiseHandler.updateName(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();

        verify(franchiseServicePort, times(1)).updateName(anyLong(),anyString());
    }

}