package com.BrainWorks.User_Management_App.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenApi(){

        return new OpenAPI()
                .info(new Info().title("User Management Api")
                        .description("This is user management Application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"));
    }
    @Bean
    public GroupedOpenApi ControllerApi(){
        return GroupedOpenApi.builder()
                .group("Controller-Api")
                .packagesToScan("com.BrainWorks.User_Management_App")
                .build();

    }
}
