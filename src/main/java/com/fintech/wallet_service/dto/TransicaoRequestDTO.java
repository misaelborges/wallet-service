package com.fintech.wallet_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(name = "TransacaoRequest", description = "Dados para realização de uma transação financeira (depósito ou saque)")
public record TransicaoRequestDTO(
        @Positive(message = "Valor de deposito tem que ser maior que ZERO")
        @Schema(description = "Valor da transação", example = "100.00", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal valor
) {}
