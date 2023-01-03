package com.project.Project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("v1-definition")
                .packagesToScan("com.project.Project.controller")
                .build();
    }

    @Bean
    public OpenAPI eroomAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("E-Room API")
                                .description("이룸 API 명세서")
                                .version("0.0.1")
                );
    }
}
