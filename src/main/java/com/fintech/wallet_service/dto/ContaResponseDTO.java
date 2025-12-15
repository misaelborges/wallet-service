package com.fintech.wallet_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Schema(name = "ContaResponse", description = "Representação completa de uma conta")
public record ContaResponseDTO(

        @Schema(description = "ID da conta", example = "1")
        Long id,

        @Schema(description = "ID do usuário dono da conta", example = "10")
        Long usuarioId,

        @Schema(description = "Número da conta no formato ACC-XXXXXXXXXX", example = "ACC-1234567890")
        String numeroConta,

        @Schema(description = "Saldo atual da conta", example = "1500.75")
        BigDecimal saldo,

        @Schema(description = "Tipo da conta", example = "CORRENTE")
        String tipo,

        @Schema(description = "Data e hora da criação da conta", example = "2025-01-01T10:15:30-03:00")
        OffsetDateTime dataCriacao,

        @Schema(description = "Data e hora da última atualização da conta", example = "2025-01-02T14:20:00-03:00")
        OffsetDateTime dataAtualizacao,

        @Schema(description = "Indica se a conta está ativa", example = "true")
        Boolean ativa
) {
}

