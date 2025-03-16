package com.example.franchises.infrastructure.entrypoints.handlers;

import com.example.franchises.domain.api.IFranchiseServicePort;
import com.example.franchises.domain.exceptions.BadRequestException;
import com.example.franchises.domain.exceptions.FranchiseAlreadyExistException;
import com.example.franchises.domain.models.Franchise;
import com.example.franchises.infrastructure.entrypoints.dtos.franchise.FranchiseResponseDto;
import com.example.franchises.infrastructure.entrypoints.dtos.franchise.FranchiseSaveDto;
import com.example.franchises.infrastructure.entrypoints.mappers.IFranchiseHandlerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

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


}