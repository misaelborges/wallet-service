package com.fintech.wallet_service.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "ContaRequest", description = "Dados necessários para criação de uma conta")
public record ContaRequestDTO(

        @NotNull(message = "O id do usuario é obrigatório")
        @Schema(description = "ID do usuário dono da conta", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
        Long usuarioId,

        @NotBlank(message = "Tipo de conta é obrigatório")
        @Schema(description = "Tipo da conta", example = "CORRENTE", requiredMode = Schema.RequiredMode.REQUIRED)
        String tipo
) {
}
