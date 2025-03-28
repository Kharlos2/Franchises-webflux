package com.example.franchises.infrastructure.entrypoints.handlers;

import com.example.franchises.domain.api.IProductServicePort;
import com.example.franchises.domain.exceptions.*;
import com.example.franchises.infrastructure.entrypoints.dtos.ErrorDto;
import com.example.franchises.infrastructure.entrypoints.dtos.UpdateNameDto;
import com.example.franchises.infrastructure.entrypoints.dtos.product.ProductSaveDto;
import com.example.franchises.infrastructure.entrypoints.dtos.product.ProductUpdateStockDto;
import com.example.franchises.infrastructure.entrypoints.mappers.IProductHandlerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.example.franchises.infrastructure.entrypoints.utils.Constants.INVALID_LONG_PARAMETER;
import static com.example.franchises.infrastructure.entrypoints.utils.Constants.SERVER_ERROR;


@Component
public class ProductHandler {

    private static final Logger log = LoggerFactory.getLogger(ProductHandler.class);
    private final IProductServicePort productServicePort;
    private final IProductHandlerMapper productHandlerMapper;

    public ProductHandler(IProductServicePort productServicePort, IProductHandlerMapper productHandlerMapper) {
        this.productServicePort = productServicePort;
        this.productHandlerMapper = productHandlerMapper;
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(ProductSaveDto.class)
                .map(productHandlerMapper::toModel)
                .flatMap(productServicePort::save)
                .map(productHandlerMapper::toProductResponseDTO)
                .flatMap(productResponse -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(productResponse))
                .onErrorResume(BranchNotFoundException.class, e ->
                        ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(ProductAlreadyExistException.class, e ->
                        ServerResponse.status(HttpStatus.CONFLICT).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(e ->{
                        log.error(e.getMessage(),e);
                        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new ErrorDto(SERVER_ERROR));

                });
    }
    public Mono<ServerResponse> delete(ServerRequest request) {
        return Mono.justOrEmpty(Optional.of(request.pathVariable("id")))
                .map(Long::parseLong)
                .flatMap(id -> productServicePort.deleteRelationWithBranch(id).thenReturn(id))
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .build())
                .onErrorResume(ProductNotFoundException.class, e ->
                        ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(NumberFormatException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(new ErrorDto(INVALID_LONG_PARAMETER)))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(e ->{
                    log.error(e.getMessage(),e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new ErrorDto(SERVER_ERROR));

                });
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        return Mono.just(request.pathVariable("id"))
                .map(Long::parseLong)
                .flatMap(id -> request.bodyToMono(ProductUpdateStockDto.class)
                        .flatMap(dto -> productServicePort.updateStock(id, dto.getStock()))
                        .map(productHandlerMapper::toProductResponseDTO)
                        .flatMap(response -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(response)))
                .onErrorResume(ProductNotFoundException.class, e ->
                        ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(NumberFormatException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(new ErrorDto(ExceptionsEnum.INCORRECT_ID.getMessage())))
                .onErrorResume(e -> {
                    log.error(e.getMessage(),e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new ErrorDto(SERVER_ERROR));

                });
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        return Mono.just(request.pathVariable("id"))
                .map(Long::parseLong)
                .flatMap(id ->
                        request.bodyToMono(UpdateNameDto.class)
                                .flatMap(dto -> productServicePort.updateName(id,dto.getName()))
                                .map(productHandlerMapper::toProductResponseDTO)
                                .flatMap(response -> ServerResponse.status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response)))

                .onErrorResume(ProductNotFoundException.class, e ->
                        ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(ProductAlreadyExistException.class, e ->
                        ServerResponse.status(HttpStatus.CONFLICT).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(NumberFormatException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(new ErrorDto(ExceptionsEnum.INCORRECT_ID.getMessage())))
                .onErrorResume(e ->{
                    log.error(e.getMessage(),e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new ErrorDto(SERVER_ERROR));

                });
    }
}
