package com.example.franchises.infrastructure.entrypoints.handlers;

import com.example.franchises.domain.api.IBranchServicePort;
import com.example.franchises.domain.exceptions.BadRequestException;
import com.example.franchises.domain.exceptions.BranchAlreadyExistException;
import com.example.franchises.domain.exceptions.BranchNotFoundException;
import com.example.franchises.domain.exceptions.FranchiseNotFoundException;
import com.example.franchises.domain.models.Branch;
import com.example.franchises.infrastructure.entrypoints.dtos.UpdateNameDto;
import com.example.franchises.infrastructure.entrypoints.dtos.branch.BranchResponseDto;
import com.example.franchises.infrastructure.entrypoints.dtos.branch.BranchSaveDto;
import com.example.franchises.infrastructure.entrypoints.mappers.IBranchHandlerMapper;
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
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchHandlerTest {


    @InjectMocks
    private BranchHandler branchHandler;

    @Mock
    private IBranchServicePort branchServicePort;

    @Mock
    private IBranchHandlerMapper branchHandlerMapper;

    private Branch branch;
    private BranchSaveDto branchSaveDto;
    private BranchResponseDto responseDto;
    private UpdateNameDto updateNameDto;

    @BeforeEach
    void setUp() {
        branch = new Branch(1L, "Sucursal 1", 2L);
        branchSaveDto = new BranchSaveDto("Sucursal 1", 2L);
        responseDto = new BranchResponseDto(1L, "Sucursal 1");
        updateNameDto = new UpdateNameDto("New name");
    }

    @Test
    void testSaveBranch_Success() {

        when(branchHandlerMapper.toModel(any(BranchSaveDto.class))).thenReturn(branch);
        when(branchServicePort.save(any(Branch.class))).thenReturn(Mono.just(branch));
        when(branchHandlerMapper.toBranchResponseDTO(any(Branch.class))).thenReturn(responseDto);

        ServerRequest request = MockServerRequest.builder()
                .body(Mono.just(branchSaveDto));

        Mono<ServerResponse> responseMono = branchHandler.save(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.CREATED))
                .verifyComplete();
    }

    @Test
    void testSaveBranch_FranchiseNotFound() {
        BranchSaveDto dto = new BranchSaveDto("Sucursal 1", 1L);

        when(branchHandlerMapper.toModel(any(BranchSaveDto.class))).thenReturn(new Branch(1L, "Sucursal 1", 1L));
        when(branchServicePort.save(any(Branch.class)))
                .thenReturn(Mono.error(new FranchiseNotFoundException("Franquicia no encontrada")));

        ServerRequest request = MockServerRequest.builder().body(Mono.just(dto));

        Mono<ServerResponse> responseMono = branchHandler.save(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    void testSaveBranch_InternalServerError() {
        BranchSaveDto dto = new BranchSaveDto("Sucursal 1", 1L);

        when(branchHandlerMapper.toModel(any(BranchSaveDto.class))).thenReturn(new Branch(1L, "Sucursal 1", 1L));
        when(branchServicePort.save(any(Branch.class)))
                .thenReturn(Mono.error(new RuntimeException("Error inesperado")));

        ServerRequest request = MockServerRequest.builder().body(Mono.just(dto));

        Mono<ServerResponse> responseMono = branchHandler.save(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();
    }

    @Test
    void testSaveBranch_Conflict() {
        BranchSaveDto dto = new BranchSaveDto("Sucursal 1", 1L);

        when(branchHandlerMapper.toModel(any(BranchSaveDto.class))).thenReturn(new Branch(1L, "Sucursal 1", 1L));
        when(branchServicePort.save(any(Branch.class)))
                .thenReturn(Mono.error(new BranchAlreadyExistException("Branch Already exist")));

        ServerRequest request = MockServerRequest.builder().body(Mono.just(dto));

        Mono<ServerResponse> responseMono = branchHandler.save(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.CONFLICT))
                .verifyComplete();
    }

    @Test
    void testSaveBranch_BadRequest() {
        BranchSaveDto dto = new BranchSaveDto("Sucursal 1", 1L);

        when(branchHandlerMapper.toModel(any(BranchSaveDto.class))).thenReturn(new Branch(1L, "Sucursal 1", 1L));
        when(branchServicePort.save(any(Branch.class)))
                .thenReturn(Mono.error(new BadRequestException("Bad Request")));

        ServerRequest request = MockServerRequest.builder().body(Mono.just(dto));

        Mono<ServerResponse> responseMono = branchHandler.save(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }


    @Test
    void testUpdateBranchName_Success() {



        when(branchServicePort.updateName(anyLong(),anyString())).thenReturn(Mono.just(branch));
        when(branchHandlerMapper.toBranchResponseDTO(any(Branch.class))).thenReturn(responseDto);

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/branch"))
                .pathVariable("id","1")
                .body(Mono.just(updateNameDto));

        Mono<ServerResponse> responseMono = branchHandler.updateName(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void testUpdateBranchName_NotFound() {
        

        when(branchServicePort.updateName(anyLong(),anyString()))
                .thenReturn(Mono.error(new BranchNotFoundException("Sucursal no encontrada")));

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/branch"))
                .pathVariable("id","1")
                .body(Mono.just(updateNameDto));

        Mono<ServerResponse> responseMono = branchHandler.updateName(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    void testUpdateBranchName_Conflict() {
        


        when(branchServicePort.updateName(anyLong(),anyString()))
                .thenReturn(Mono.error(new BranchAlreadyExistException("Sucursal no encontrada")));

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/branch"))
                .pathVariable("id","1")
                .body(Mono.just(updateNameDto));

        Mono<ServerResponse> responseMono = branchHandler.updateName(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.CONFLICT))
                .verifyComplete();
    }

    @Test
    void testUpdateBranchName_BadRequest() {

        when(branchServicePort.updateName(anyLong(),anyString()))
                .thenReturn(Mono.error(new BadRequestException("Bad request")));

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/branch"))
                .pathVariable("id","1")
                .body(Mono.just(updateNameDto));

        Mono<ServerResponse> responseMono = branchHandler.updateName(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    @Test
    void testUpdateBranchName_InternalError() {

        when(branchServicePort.updateName(anyLong(),anyString()))
                .thenReturn(Mono.error(new RuntimeException("Error")));

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/branch"))
                .pathVariable("id","1")
                .body(Mono.just(updateNameDto));

        Mono<ServerResponse> responseMono = branchHandler.updateName(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();
    }

}