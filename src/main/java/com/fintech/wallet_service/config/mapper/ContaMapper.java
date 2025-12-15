package com.fintech.wallet_service.config.mapper;

import com.fintech.wallet_service.dto.ContaRequestDTO;
import com.fintech.wallet_service.dto.ContaResponseDTO;
import com.fintech.wallet_service.dto.ContaResumoResponseDTO;
import com.fintech.wallet_service.dto.SaldoResponseDTO;
import com.fintech.wallet_service.entity.Conta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContaMapper {

    Conta toEntity(ContaRequestDTO contaRequestDTO);
    ContaResponseDTO toResponseDTO(Conta conta);
    List<ContaResumoResponseDTO> toResponseDTO(List<Conta> conta);

    @Mapping(target = "dataConsulta", expression = "java(OffsetDateTime.now())")
    SaldoResponseDTO toSaldoResponseDTO(Conta conta);
}
