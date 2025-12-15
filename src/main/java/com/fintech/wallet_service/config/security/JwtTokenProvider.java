package com.fintech.wallet_service.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

@Component
public class JwtTokenProvider {

    private static final Logger logger = Logger.getLogger(JwtTokenProvider.class.getName());
    private String secret = "secret";

    public String extrairToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public boolean validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                    .build()
                    .verify(token);
            logger.info("Token validado com sucesso!");
            return true;
        } catch (JWTVerificationException ex) {
            logger.warning("Token inválido: " + ex.getMessage());
            return false;
        } catch (Exception e) {
            logger.warning("Erro ao validar token: " + e.getMessage());
            return false;
        }
    }

    public String obterUsuarioIdDoToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(token);

            Long usuarioId = decodedJWT.getClaim("userId").asLong();
            logger.info("UsuarioId extraído do token: " + usuarioId);
            return usuarioId.toString();
        } catch (Exception e) {
            logger.warning("Erro ao extrair usuarioId: " + e.getMessage());
            return null;
        }
    }
}