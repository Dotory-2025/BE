package com.dotoryteam.dotory.global.common.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SCHEMA_NAME = "Authorization";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .components(appAuthorization())
                .addSecurityItem(security())
                .addServersItem(new Server().url("http://localhost:8080"));
//                .addServersItem(new Server().url("https://여기 도메인 산거 나중에 연결하면 주석해제해주세욤"));
    }

    private Info apiInfo() {
        return new Info()
                .title("Dotory")
                .description("도토리 프로젝트의 REST API를 볼 수 있는 스웨거~")
                .version("1.0.0");
    }

    private Components appAuthorization() {
        return new Components()
                .addSecuritySchemes(SCHEMA_NAME, new SecurityScheme()
                        .name(SCHEMA_NAME)
                        .type(Type.APIKEY)
                        .in(In.HEADER));
    }

    private SecurityRequirement security() {
        return new SecurityRequirement().addList(SCHEMA_NAME);
    }

}