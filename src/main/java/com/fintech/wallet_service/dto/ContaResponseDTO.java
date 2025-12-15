package com.fintech.wallet_service.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ContaResponseDTO(
        Long id,
        Long usuarioId,
        String numeroConta,
        BigDecimal saldo,
        String tipo,
        OffsetDateTime dataCriacao,
        OffsetDateTime dataAtualizacao,
        Boolean ativa
) {
}
