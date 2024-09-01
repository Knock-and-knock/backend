package com.shinhan.knockknock.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        Info info = new Info()
                .version("v0.0.1")
                .title("Knock Knock API 명세")
                .description("Knock Knock의 API 명세입니다.");

        List<Server> servers = getServers();

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
                .servers(servers) // 서버 리스트에 추가
                .info(info);
    }

    @NotNull
    private static List<Server> getServers() {
        List<Server> servers = new ArrayList<>();

        // 두 번째 서버 설정: 현재 사용 중인 서버
        Server productionServer = new Server();
        productionServer.setUrl("https://ds-knock-knock.duckdns.org");
        productionServer.setDescription("Production Server");
        servers.add(productionServer);

        // 첫 번째 서버 설정: 로컬 서버
        Server localServer = new Server();
        localServer.setUrl("http://localhost:9090");
        localServer.setDescription("Local Server");
        servers.add(localServer);
        return servers;
    }

}
