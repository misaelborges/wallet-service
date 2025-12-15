package com.fintech.wallet_service.controller;

import com.fintech.wallet_service.dto.ContaRequestDTO;
import com.fintech.wallet_service.dto.ContaResponseDTO;
import com.fintech.wallet_service.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
