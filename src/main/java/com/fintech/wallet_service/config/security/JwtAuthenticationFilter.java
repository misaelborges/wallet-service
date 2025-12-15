package com.fintech.wallet_service.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        logger.info("===== JWT FILTER DEBUG =====");
        logger.info("Auth Header recebido: " + authHeader);

        String token = jwtTokenProvider.extrairToken(authHeader);
        logger.info("Token extraído: " + token);

        if (token != null) {
            boolean isValid = jwtTokenProvider.validarToken(token);
            logger.info("Token é válido? " + isValid);

            if (isValid) {
                String usuarioId = jwtTokenProvider.obterUsuarioIdDoToken(token);
                logger.info("UsuarioId extraído: " + usuarioId);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(usuarioId, token, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Authentication definido com sucesso!");
            } else {
                logger.warning("Token inválido!");
            }
        } else {
            logger.warning("Token é null!");
        }

        filterChain.doFilter(request, response);
    }
}
