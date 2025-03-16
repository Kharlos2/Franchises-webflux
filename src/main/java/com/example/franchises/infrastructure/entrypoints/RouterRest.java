package com.example.franchises.infrastructure.entrypoints;

import com.example.franchises.infrastructure.entrypoints.dtos.branch.BranchResponseDto;
import com.example.franchises.infrastructure.entrypoints.dtos.branch.BranchSaveDto;
import com.example.franchises.infrastructure.entrypoints.dtos.franchise.FranchiseResponseDto;
import com.example.franchises.infrastructure.entrypoints.dtos.franchise.FranchiseSaveDto;
import com.example.franchises.infrastructure.entrypoints.dtos.product.ProductResponseDto;
import com.example.franchises.infrastructure.entrypoints.dtos.product.ProductSaveDto;
import com.example.franchises.infrastructure.entrypoints.handlers.BranchHandler;
import com.example.franchises.infrastructure.entrypoints.handlers.FranchiseHandler;
import com.example.franchises.infrastructure.entrypoints.handlers.ProductHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @RouterOperations({
            @RouterOperation(
                    path = "/franchise",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            operationId = "save",
                            summary = "Franchise Creation",
                            requestBody = @RequestBody(
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = FranchiseSaveDto.class,
                                                    example = "{ \\\"name\\\": \\\"Franchise A\\\" }"

                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Successful creation",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = FranchiseResponseDto.class
                                                    )
                                            )
                                    )
                            }
                    )


            ),
            @RouterOperation(
                    path = "/health",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "healthCheck",
                    operation = @Operation(
                            operationId = "healthCheck",
                            summary = "Health Check",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful get"
                                    )
                            }
                    )

            )
    })
    @Bean
    public RouterFunction<ServerResponse> routerFranchiseFunction(FranchiseHandler franchiseHandler) {
        return route(POST("/franchise"), franchiseHandler::save)
                .andRoute(GET("/health"), request -> franchiseHandler.healthCheck())

                ;
    }

    @RouterOperations({
            @RouterOperation(
                    path = "/branch",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = BranchHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            operationId = "save",
                            summary = "Branch Creation",
                            requestBody = @RequestBody(
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = BranchSaveDto.class,
                                                    example = "{ \\\"name\\\": \\\"Branch A\\\", \\\"franchiseId\\\": \\\"1\\\"}"

                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Successful creation",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = BranchResponseDto.class
                                                    )
                                            )
                                    )
                            }
                    )

            )
    })
    @Bean
    public RouterFunction<ServerResponse> routerBranchFunction(BranchHandler branchHandler) {
        return route(POST("/branch"), branchHandler::save)

                ;
    }


    @RouterOperations({
            @RouterOperation(
                    path = "/product",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = ProductHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            operationId = "save",
                            summary = "Product Creation",
                            requestBody = @RequestBody(
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = ProductSaveDto.class,
                                                    example = "{ \\\"name\\\": \\\"Product A\\\" }"

                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Successful creation",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = ProductResponseDto.class
                                                    )
                                            )
                                    )
                            }
                    )

            ),
            @RouterOperation(
                    path = "/product",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.DELETE,
                    beanClass = ProductHandler.class,
                    beanMethod = "delete",
                    operation = @Operation(
                            operationId = "delete",
                            summary = "Product delete",
                            parameters = {
                                    @Parameter(name = "id", schema = @Schema(type = "Long", defaultValue = "0"))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful delete"
                                    )
                            }
                    )

            ),
    })
    @Bean
    public RouterFunction<ServerResponse> routerProductFunction(ProductHandler productHandler) {
        return route(POST("/product"), productHandler::save)
                .andRoute(DELETE("/product"), productHandler::delete)

                ;
    }
}
