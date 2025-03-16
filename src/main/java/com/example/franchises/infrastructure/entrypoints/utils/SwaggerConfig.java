package com.example.franchises.infrastructure.entrypoints.utils;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Springweb flux Franchise functional endpoint .",
        version = "1.0",
        description = "swagger franchise documentation"
))
public class SwaggerConfig {

}