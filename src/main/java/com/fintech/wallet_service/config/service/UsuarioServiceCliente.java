package com.fintech.wallet_service.config.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.logging.Logger;

@Component
public class UsuarioServiceCliente {

    private static final Logger logger = Logger.getLogger(UsuarioServiceCliente.class.getName());
    private final WebClient webClient;

    public UsuarioServiceCliente(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8080").build();
    }

    public boolean usuarioExiste(Long usuarioId) {
        try {
            String token = obterTokenDoContexto();
            logger.info("Token extraído: " + token);

            webClient.get()
                    .uri("/api/v1/usuarios/{usuarioId}", usuarioId)
                    .header("Authorization", token)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            return true;
        } catch (WebClientResponseException.NotFound e) {
            return false;
        } catch (WebClientResponseException.Forbidden e) {
            logger.severe("Token rejeitado pelo user-service: " + e.getMessage());
            throw new RuntimeException("Token inválido ou expirado");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar usuario: " + e.getMessage());
        }
    }

    private String obterTokenDoContexto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authentication: " + authentication);
        logger.info("Credentials: " + (authentication != null ? authentication.getCredentials() : "null"));

        if (authentication != null && authentication.getCredentials() != null) {
            String token = authentication.getCredentials().toString();

            if (token.startsWith("Bearer ")) {
                return token;
            }

            return "Bearer " + token;
        }

        throw new RuntimeException("Token não encontrado no contexto de segurança");
    }
}