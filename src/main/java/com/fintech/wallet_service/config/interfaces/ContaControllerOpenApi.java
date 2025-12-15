package com.fintech.wallet_service.config.interfaces;

import com.fintech.wallet_service.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Conta", description = "Gerenciamento de contas bancárias do usuário")
public interface ContaControllerOpenApi {

    @Operation(summary = "Criar uma nova conta", description = "Cria uma nova conta vinculada a um usuário")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Conta criada com sucesso",
                    content = @Content(schema = @Schema(implementation = ContaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    ResponseEntity<ContaResponseDTO> criarConta(
            @Parameter(description = "Dados para criação da conta", required = true) ContaRequestDTO contaRequestDTO);

    @Operation(
            summary = "Buscar conta por ID",
            description = "Busca os dados de uma conta pelo seu identificador"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Conta encontrada",
                    content = @Content(schema = @Schema(implementation = ContaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    ResponseEntity<ContaResponseDTO> buscarCarteiraPorId(@Parameter(
                    description = "ID da conta",
                    example = "1",
                    required = true
            ) Long id
    );


    @Operation(
            summary = "Listar contas do usuário",
            description = "Lista todas as contas vinculadas a um usuário"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de contas retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = ContaResumoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    ResponseEntity<List<ContaResumoResponseDTO>> listarCarteirasUsuario(
            @Parameter(
                    description = "ID do usuário",
                    example = "10",
                    required = true
            )
            Long usuarioId
    );

    @Operation(
            summary = "Consultar saldo",
            description = "Consulta o saldo atual de uma conta"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Saldo retornado com sucesso",
                    content = @Content(schema = @Schema(implementation = SaldoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    ResponseEntity<SaldoResponseDTO> consultarSaldo(
            @Parameter(
                    description = "ID da conta",
                    example = "1",
                    required = true
            )
            Long id
    );

    @Operation(
            summary = "Depositar valor",
            description = "Realiza um depósito em uma conta"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Depósito realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = SaldoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Valor inválido"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    ResponseEntity<SaldoResponseDTO> depositar(
            @Parameter(
                    description = "ID da conta",
                    example = "1",
                    required = true
            )
            Long id,

            @Parameter(description = "Dados da transação de depósito", required = true)
            TransicaoRequestDTO transicaoRequestDTO
    );

    @Operation(
            summary = "Sacar valor",
            description = "Realiza um saque em uma conta"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Saque realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = SaldoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Saldo insuficiente ou valor inválido"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    ResponseEntity<SaldoResponseDTO> sacar(
            @Parameter(
                    description = "ID da conta",
                    example = "1",
                    required = true
            )
            Long id,

            @Parameter(description = "Dados da transação de saque", required = true)
            TransicaoRequestDTO transicaoRequestDTO
    );
}

