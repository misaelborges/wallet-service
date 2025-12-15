package com.fintech.wallet_service.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(Long id) {
        super("Não foi encontrado um Usúario com o id: %d" + id);
    }
}
