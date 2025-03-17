package com.example.franchises.infrastructure.entrypoints.handlers;

import com.example.franchises.domain.api.IProductServicePort;
import com.example.franchises.domain.exceptions.BadRequestException;
import com.example.franchises.domain.exceptions.BranchNotFoundException;
import com.example.franchises.domain.exceptions.ProductAlreadyExistException;
import com.example.franchises.domain.exceptions.ProductNotFoundException;
import com.example.franchises.domain.models.Product;
import com.example.franchises.infrastructure.entrypoints.dtos.UpdateNameDto;
import com.example.franchises.infrastructure.entrypoints.dtos.product.ProductResponseDto;
import com.example.franchises.infrastructure.entrypoints.dtos.product.ProductSaveDto;
import com.example.franchises.infrastructure.entrypoints.dtos.product.ProductUpdateStockDto;
import com.example.franchises.infrastructure.entrypoints.mappers.IProductHandlerMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductHandlerTest {
    @Mock
    private IProductServicePort productServicePort;

    @Mock
    private IProductHandlerMapper productHandlerMapper;

    @InjectMocks
    private ProductHandler productHandler;

    private ProductSaveDto dto;
    private ProductResponseDto responseDTO;
    private Product product;
    private ProductUpdateStockDto updateStockDto;
    private UpdateNameDto updateNameDto;

    @BeforeEach
    void setUp() {
        dto = new ProductSaveDto("Test product",1,1L);
        responseDTO = new ProductResponseDto(1L, "Test product",1);
        product = new Product(1L,"Test Franchise",2,1L);
        updateStockDto = new ProductUpdateStockDto(10);
        updateNameDto = new UpdateNameDto("NuevoNombre");
    }

    @Test
    void testSaveSuccess(){
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/Product"))
                .body(Mono.just(dto));
        when(productHandlerMapper.toModel(any())).thenReturn(product);
        when(productServicePort.save(any())).thenReturn(Mono.just(product));
        when(productHandlerMapper.toProductResponseDTO(any())).thenReturn(responseDTO);


        StepVerifier.create(productHandler.save(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.CREATED))
                .verifyComplete();

        verify(productHandlerMapper, times(1)).toModel(dto);
        verify(productServicePort, times(1)).save(product);
    }
    @Test
    void testSaveNotFound(){
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/Product"))
                .body(Mono.just(dto));
        when(productHandlerMapper.toModel(any())).thenReturn(product);
        when(productServicePort.save(any())).thenReturn(Mono.error(new BranchNotFoundException("Branch not found")));


        StepVerifier.create(productHandler.save(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();

        verify(productHandlerMapper, times(1)).toModel(dto);
        verify(productServicePort, times(1)).save(product);
    }
    @Test
    void testSaveConflict(){

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/Product"))
                .body(Mono.just(dto));
        when(productHandlerMapper.toModel(any())).thenReturn(product);
        when(productServicePort.save(any())).thenReturn(Mono.error(new ProductAlreadyExistException("Product already exist")));


        StepVerifier.create(productHandler.save(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.CONFLICT))
                .verifyComplete();

        verify(productHandlerMapper, times(1)).toModel(dto);
        verify(productServicePort, times(1)).save(product);
    }
    @Test
    void testSaveBadRequest(){
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/Product"))
                .body(Mono.just(dto));
        when(productHandlerMapper.toModel(any())).thenReturn(product);
        when(productServicePort.save(any())).thenReturn(Mono.error(new BadRequestException("Bad request")));


        StepVerifier.create(productHandler.save(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();

        verify(productHandlerMapper, times(1)).toModel(dto);
        verify(productServicePort, times(1)).save(product);
    }
    @Test
    void testSaveServerError(){
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/Product"))
                .body(Mono.just(dto));
        when(productHandlerMapper.toModel(any())).thenReturn(product);
        when(productServicePort.save(any())).thenReturn(Mono.error(new RuntimeException("Error")));


        StepVerifier.create(productHandler.save(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();

        verify(productHandlerMapper, times(1)).toModel(dto);
        verify(productServicePort, times(1)).save(product);
    }
    @Test
    void testDeleteSuccess() {
        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", "1")
                .build();
        when(productServicePort.deleteRelationWithBranch(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productHandler.delete(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(productServicePort, times(1)).deleteRelationWithBranch(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", "1")
                .build();
        when(productServicePort.deleteRelationWithBranch(1L)).thenReturn(Mono.error(new ProductNotFoundException("Product not found")));

        StepVerifier.create(productHandler.delete(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();

        verify(productServicePort, times(1)).deleteRelationWithBranch(1L);
    }

    @Test
    void testDeleteInvalidProductId() {
        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", "abc")
                .build();

        StepVerifier.create(productHandler.delete(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }


    @Test
    void testDeleteBadRequest() {
        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", "1")
                .build();
        when(productServicePort.deleteRelationWithBranch(1L)).thenReturn(Mono.error(new BadRequestException("Invalid request")));

        StepVerifier.create(productHandler.delete(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();

        verify(productServicePort, times(1)).deleteRelationWithBranch(1L);
    }

    @Test
    void testDeleteServerError() {
        ServerRequest request = MockServerRequest.builder()
                .pathVariable("id", "1")
                .build();
        when(productServicePort.deleteRelationWithBranch(1L)).thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        StepVerifier.create(productHandler.delete(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();

        verify(productServicePort, times(1)).deleteRelationWithBranch(1L);
    }

    @Test
    void testUpdateStockSuccess() {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .pathVariable("id", "1")
                .body(Mono.just(updateStockDto));

        when(productServicePort.updateStock(anyLong(), anyInt())).thenReturn(Mono.just(product));
        when(productHandlerMapper.toProductResponseDTO(any())).thenReturn(responseDTO);

        StepVerifier.create(productHandler.updateStock(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(productServicePort, times(1)).updateStock(1L, 10);
    }

    @Test
    void testUpdateStockNotFound() {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .pathVariable("id", "1")
                .body(Mono.just(updateStockDto));

        when(productServicePort.updateStock(anyLong(), anyInt()))
                .thenReturn(Mono.error(new ProductNotFoundException("Product not found")));

        StepVerifier.create(productHandler.updateStock(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();

        verify(productServicePort, times(1)).updateStock(1L, 10);
    }

    @Test
    void testUpdateStockFormatException() {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .pathVariable("id", "abc")
                .body(Mono.just(updateStockDto));

        StepVerifier.create(productHandler.updateStock(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }
    @Test
    void testUpdateStockBadRequest() {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .pathVariable("id", "1")
                .body(Mono.just(updateStockDto));

        when(productServicePort.updateStock(anyLong(), anyInt()))
                .thenReturn(Mono.error(new BadRequestException("Negative id")));

        StepVerifier.create(productHandler.updateStock(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();

        verify(productServicePort, times(1)).updateStock(1L, 10);
    }
    @Test
    void testUpdateStockServerError() {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .pathVariable("id", "1")
                .body(Mono.just(updateStockDto));

        when(productServicePort.updateStock(anyLong(), anyInt()))
                .thenReturn(Mono.error(new RuntimeException("Internal Server Error")));

        StepVerifier.create(productHandler.updateStock(request))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();

        verify(productServicePort, times(1)).updateStock(1L, 10);
    }

    @Test
    void testUpdateProductName_Success() {
        when(productServicePort.updateName(anyLong(), anyString())).thenReturn(Mono.just(product));
        when(productHandlerMapper.toProductResponseDTO(any(Product.class))).thenReturn(responseDTO);

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/product"))
                .pathVariable("id", "1")
                .body(Mono.just(updateNameDto));

        Mono<ServerResponse> responseMono = productHandler.updateName(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void testUpdateProductName_NotFound() {
        when(productServicePort.updateName(anyLong(), anyString()))
                .thenReturn(Mono.error(new ProductNotFoundException("Producto no encontrado")));

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/product"))
                .pathVariable("id", "1")
                .body(Mono.just(updateNameDto));

        Mono<ServerResponse> responseMono = productHandler.updateName(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    void testUpdateProductName_Conflict() {
        when(productServicePort.updateName(anyLong(), anyString()))
                .thenReturn(Mono.error(new ProductAlreadyExistException("Producto ya existe")));

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/product"))
                .pathVariable("id", "1")
                .body(Mono.just(updateNameDto));

        Mono<ServerResponse> responseMono = productHandler.updateName(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.CONFLICT))
                .verifyComplete();
    }

    @Test
    void testUpdateProductName_BadRequest() {
        when(productServicePort.updateName(anyLong(), anyString()))
                .thenReturn(Mono.error(new BadRequestException("Bad request")));

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/product"))
                .pathVariable("id", "1")
                .body(Mono.just(updateNameDto));

        Mono<ServerResponse> responseMono = productHandler.updateName(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    @Test
    void testUpdateProductName_InternalError() {
        when(productServicePort.updateName(anyLong(), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Error inesperado")));

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/product"))
                .pathVariable("id", "1")
                .body(Mono.just(updateNameDto));

        Mono<ServerResponse> responseMono = productHandler.updateName(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();
    }

    @Test
    void testUpdateProductName_InvalidParameter() {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/product"))
                .pathVariable("id", "A")
                .body(Mono.just(updateNameDto));

        Mono<ServerResponse> responseMono = productHandler.updateName(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

}