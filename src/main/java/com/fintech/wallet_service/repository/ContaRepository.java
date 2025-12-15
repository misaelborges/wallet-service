package com.fintech.wallet_service.repository;

import com.fintech.wallet_service.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findAllContaByUsuarioId(Long usuarioId);
}
