package com.fintech.wallet_service.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SaldoResponseDTO(
        Long id,
        String numeroConta,
        BigDecimal saldo,
        String tipo,
        OffsetDateTime dataConsulta
) {}
