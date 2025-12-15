package com.fintech.wallet_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ContaResumoResponse", description = "Resumo de informações da conta")
public record ContaResumoResponseDTO(

        @Schema(description = "ID da conta", example = "1")
        Long id,

        @Schema(description = "ID do usuário dono da conta", example = "10")
        Long usuarioId,

        @Schema(description = "Número da conta", example = "ACC-1234567890")
        String numeroConta,

        @Schema(description = "Tipo da conta", example = "CORRENTE")
        String tipo
) {
}

