package com.shinhan.knockknock.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi(){
        Info info = new Info()
                .version("v1.0")
                .title("Swagger 테스트")
                .description("Knock Knock");

        return new OpenAPI()
                .info(info);
    }

}
