package com.example.franchises.infrastructure.entrypoints.handlers;

import com.example.franchises.domain.api.IBranchServicePort;
import com.example.franchises.domain.exceptions.*;
import com.example.franchises.infrastructure.entrypoints.dtos.ErrorDto;
import com.example.franchises.infrastructure.entrypoints.dtos.UpdateNameDto;
import com.example.franchises.infrastructure.entrypoints.dtos.branch.BranchSaveDto;
import com.example.franchises.infrastructure.entrypoints.mappers.IBranchHandlerMapper;
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
public class BranchHandler {
    private static final Logger log = LoggerFactory.getLogger(BranchHandler.class);

    private final IBranchServicePort branchServicePort;
    private final IBranchHandlerMapper branchHandlerMapper;

    public BranchHandler(IBranchServicePort branchServicePort, IBranchHandlerMapper branchHandlerMapper) {
        this.branchServicePort = branchServicePort;
        this.branchHandlerMapper = branchHandlerMapper;
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(BranchSaveDto.class)
                .map(branchHandlerMapper::toModel)
                .flatMap(branchServicePort::save)
                .map(branchHandlerMapper::toBranchResponseDTO)
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(FranchiseNotFoundException.class, e ->
                        ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(BranchAlreadyExistException.class, e ->
                        ServerResponse.status(HttpStatus.CONFLICT).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(e ->{
                    log.error(e.getMessage(),e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new ErrorDto(SERVER_ERROR));

                });
    }
    public Mono<ServerResponse> updateName(ServerRequest request) {
        return Mono.just(request.pathVariable("id"))
                .map(Long::parseLong)
                .flatMap(id ->
                        request.bodyToMono(UpdateNameDto.class)
                                .flatMap(dto -> branchServicePort.updateName(id,dto.getName()))
                                .map(branchHandlerMapper::toBranchResponseDTO)
                                .flatMap(response -> ServerResponse.status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response)))

                .onErrorResume(BranchNotFoundException.class, e ->
                        ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(NumberFormatException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(new ErrorDto(INVALID_LONG_PARAMETER)))
                .onErrorResume(BranchAlreadyExistException.class, e ->
                        ServerResponse.status(HttpStatus.CONFLICT).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(new ErrorDto(e.getMessage())))
                .onErrorResume(e ->{
                    log.error(e.getMessage(),e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new ErrorDto(SERVER_ERROR));

                });
    }

}
