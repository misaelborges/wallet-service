package com.fintech.wallet_service.service;

import com.fintech.wallet_service.config.mapper.ContaMapper;
import com.fintech.wallet_service.config.service.UsuarioServiceCliente;
import com.fintech.wallet_service.dto.ContaRequestDTO;
import com.fintech.wallet_service.dto.ContaResponseDTO;
import com.fintech.wallet_service.entity.Conta;
import com.fintech.wallet_service.exception.ContaNaoEncontradaException;
import com.fintech.wallet_service.repository.ContaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ContaMapper contaMapper;
    private final UsuarioServiceCliente usuarioServiceCliente;

    public ContaService(ContaRepository contaRepository, ContaMapper contaMapper, UsuarioServiceCliente usuarioServiceCliente) {
        this.contaRepository = contaRepository;
        this.contaMapper = contaMapper;
        this.usuarioServiceCliente = usuarioServiceCliente;
    }

    @Transactional
    public ContaResponseDTO criarConta(ContaRequestDTO contaRequestDTO) {
        if (!usuarioServiceCliente.usuarioExiste(contaRequestDTO.usuarioId())) {
            throw new RuntimeException("Usuario não foi contrado");
        }
        Conta conta = contaMapper.toEntity(contaRequestDTO);
        conta.setNumeroConta(gerarNumeroConta());
        conta.setSaldo(BigDecimal.ZERO);
        conta.setAtiva(true);
        Conta contaSalvar = contaRepository.save(conta);
        return contaMapper.toResponseDTO(contaSalvar);
    }

    @Transactional
    public ContaResponseDTO buscarCarteiraPorId(Long id) {
        Conta conta = buscaCarteira(id);
        return contaMapper.toResponseDTO(conta);
    }

    private Conta buscaCarteira(Long id) {
       return contaRepository.findById(id).orElseThrow(() -> new ContaNaoEncontradaException(id));
    }

    private String gerarNumeroConta() {
        Random random = new Random();
        StringBuilder numero = new StringBuilder("ACC-");

        for (int i = 0; i < 10; i++) {
            numero.append(random.nextInt(10));
        }

        return numero.toString();
    }
}
