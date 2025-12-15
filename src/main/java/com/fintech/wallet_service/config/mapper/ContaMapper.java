package com.fintech.wallet_service.config.mapper;

import com.fintech.wallet_service.dto.ContaRequestDTO;
import com.fintech.wallet_service.dto.ContaResponseDTO;
import com.fintech.wallet_service.entity.Conta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContaMapper {

    Conta toEntity(ContaRequestDTO contaRequestDTO);
    ContaResponseDTO toResponseDTO(Conta conta);
}
