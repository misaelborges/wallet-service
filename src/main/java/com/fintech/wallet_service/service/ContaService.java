package com.fintech.wallet_service.service;

import com.fintech.wallet_service.config.mapper.ContaMapper;
import com.fintech.wallet_service.config.service.UsuarioServiceCliente;
import com.fintech.wallet_service.dto.*;
import com.fintech.wallet_service.entity.Conta;
import com.fintech.wallet_service.exception.ContaNaoEncontradaException;
import com.fintech.wallet_service.repository.ContaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
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

    @Transactional
    public List<ContaResumoResponseDTO> listarCarteirasUsuario(Long usuarioId) {
        if (!usuarioServiceCliente.usuarioExiste(usuarioId)) {
            throw new RuntimeException("Usuario não foi contrado");
        }

        List<Conta> contasUsuario = contaRepository.findAllContaByUsuarioId(usuarioId);
        return contaMapper.toResponseDTO(contasUsuario);
    }

    @Transactional
    public SaldoResponseDTO consultarSaldo(Long id) {
        Conta conta = buscaCarteira(id);
        return contaMapper.toSaldoResponseDTO(conta);

    }

    @Transactional
    public SaldoResponseDTO depositar(Long id, DepositoRequestDTO depositoRequestDTO) {
        Conta conta = buscaCarteira(id);
        conta.setSaldo(conta.getSaldo().add(depositoRequestDTO.valor()));
        return contaMapper.toSaldoResponseDTO(conta);
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
