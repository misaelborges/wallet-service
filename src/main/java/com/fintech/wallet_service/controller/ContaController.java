package com.fintech.wallet_service.controller;

import com.fintech.wallet_service.dto.ContaRequestDTO;
import com.fintech.wallet_service.dto.ContaResponseDTO;
import com.fintech.wallet_service.dto.ContaResumoResponseDTO;
import com.fintech.wallet_service.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carteiras")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<ContaResponseDTO> criarConta(@RequestBody @Valid ContaRequestDTO contaRequestDTO) {
        ContaResponseDTO contaResponseDTO = contaService.criarConta(contaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(contaResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponseDTO> buscarCarteiraPorId(@PathVariable Long id) {
        ContaResponseDTO contaResponseDTO = contaService.buscarCarteiraPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(contaResponseDTO);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ContaResumoResponseDTO>>listarCarteirasUsuario(@PathVariable Long usuarioId) {
        List<ContaResumoResponseDTO> contaResponseDTOS = contaService.listarCarteirasUsuario(usuarioId);
        return ResponseEntity.status(HttpStatus.OK).body(contaResponseDTOS);
    }
}
