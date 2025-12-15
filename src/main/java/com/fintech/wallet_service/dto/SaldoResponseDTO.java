package com.fintech.wallet_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Schema(name = "SaldoResponse", description = "Dados de retorno da consulta de saldo")
public record SaldoResponseDTO(

        @Schema(description = "ID da conta", example = "1")
        Long id,

        @Schema(description = "Número da conta", example = "ACC-1234567890")
        String numeroConta,

        @Schema(description = "Saldo atual da conta", example = "2500.00")
        BigDecimal saldo,

        @Schema(description = "Tipo da conta", example = "CORRENTE")
        String tipo,

        @Schema(description = "Data e hora da consulta do saldo", example = "2025-01-15T16:45:00-03:00")
        OffsetDateTime dataConsulta
) {
}

