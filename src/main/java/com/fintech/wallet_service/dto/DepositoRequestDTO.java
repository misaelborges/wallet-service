package com.fintech.wallet_service.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositoRequestDTO(
        @Positive(message = "Valor de deposito tem que ser maior que ZERO")
        BigDecimal valor
) {}
