package com.fintech.wallet_service.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("API - Gestão de Usuários")
                        .version("1.0.0")
                        .description("""
                                Microserviço responsável por gerenciamento de carteiras e saldos.
                                
                                Funcionalidades:
                                - Cadastro de carteira
                                - Consulta carterira por ID
                                - Consultar todas carteiras de um usuario
                                - Consultar saldo
                                - Depositar
                                - Sacar
                                
                                Desenvolvida em Java com Spring Boot.
                                """)
                        .contact(new Contact()
                                .name("Misael Borges Cancelier")
                                .email("mizaelborges2011@email.com")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )

                        .addResponses("BadRequest", new ApiResponse().description("Requisição inválida"))
                        .addResponses("NotFound", new ApiResponse().description("Usuário não encontrado"))
                        .addResponses("Conflict", new ApiResponse().description("CPF ou e-mail já cadastrado"))
                        .addResponses("Unauthorized", new ApiResponse().description("Não autenticado / token inválido"))
                        .addResponses("Forbidden", new ApiResponse().description("Acesso não autorizado"))
                        .addResponses("InternalServerError", new ApiResponse().description("Erro interno inesperado"))
                );
    }
}