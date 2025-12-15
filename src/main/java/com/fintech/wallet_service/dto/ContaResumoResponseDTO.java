package com.fintech.wallet_service.dto;

public record ContaResumoResponseDTO(
        Long id,
        Long usuarioId,
        String numeroConta,
        String tipo
) {
}
