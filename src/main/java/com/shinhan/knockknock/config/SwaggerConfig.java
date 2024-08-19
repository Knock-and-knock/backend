package com.shinhan.knockknock.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi(){
        Info info = new Info()
                .version("v0.0.1")
                .title("Knock Knock Swagger")
                .description("Knock Knock");

        // Define the Security Scheme for JWT token
        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP) // Use HTTP type with Bearer format
                .scheme("bearer") // Set the scheme to Bearer
                .bearerFormat("JWT") // Set the format to JWT
                .in(SecurityScheme.In.HEADER) // JWT token should be included in the header
                .name("Authorization"); // Header name for JWT token

        // Define the Security Requirement with the defined security scheme
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
                .addSecurityItem(securityRequirement)
                .info(info);
    }

}
