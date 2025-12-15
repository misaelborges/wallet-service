package com.fintech.wallet_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ContaNaoEncontradaException extends RuntimeException {

    public ContaNaoEncontradaException(Long id) {
        super("Não foi encontrado uma conta com o ID: " + id);
    }
}
