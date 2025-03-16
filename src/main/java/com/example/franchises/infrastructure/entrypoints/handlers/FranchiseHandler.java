package com.example.franchises.infrastructure.entrypoints.handlers;


import com.example.franchises.domain.api.IFranchiseServicePort;
import com.example.franchises.domain.exceptions.BadRequestException;
import com.example.franchises.domain.exceptions.FranchiseAlreadyExistException;
import com.example.franchises.domain.exceptions.FranchiseNotFoundException;
import com.example.franchises.infrastructure.entrypoints.dtos.ErrorDto;
import com.example.franchises.infrastructure.entrypoints.dtos.franchise.FranchiseSaveDto;
import com.example.franchises.infrastructure.entrypoints.mappers.IFranchiseHandlerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.example.franchises.infrastructure.entrypoints.utils.Constants.INVALID_LONG_PARAMETER;
import static com.example.franchises.infrastructure.entrypoints.utils.Constants.SERVER_ERROR;


@Component
public class FranchiseHandler {
    private static final Logger log = LoggerFactory.getLogger(FranchiseHandler.class);

    private final IFranchiseServicePort franchiseServicePort;
    private final IFranchiseHandlerMapper franchiseHandlerMapper;

    public FranchiseHandler(IFranchiseServicePort franchiseServicePort, IFranchiseHandlerMapper franchiseHandlerMapper) {
        this.franchiseServicePort = franchiseServicePort;
        this.franchiseHandlerMapper = franchiseHandlerMapper;
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(FranchiseSaveDto.class)
                .map(franchiseHandlerMapper::toModel)
                .flatMap(franchiseServicePort::save)
                .map(franchiseHandlerMapper::toFranchiseResponseDTO)
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(FranchiseAlreadyExistException.class, e ->
                        ServerResponse.status(HttpStatus.CONFLICT).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(e ->{
                    log.error(e.getMessage(),e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new ErrorDto(SERVER_ERROR));

                });
    }

    public Mono<ServerResponse> healthCheck (){

        return  ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    public Mono<ServerResponse> findTopProducts(ServerRequest request) {

        return Mono.justOrEmpty(request.pathVariable("id"))
                .map(Long::parseLong)
                .flatMapMany(franchiseServicePort::findProductsStock)
                .collectList()
                .flatMap(products -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(products))
                .onErrorResume(FranchiseNotFoundException.class, e ->
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

}
