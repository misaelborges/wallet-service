package com.fintech.wallet_service.Exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
    ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
    MENSGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
    ACESSO_NEGADO("/acesso-negado", "Acesso negado");

    private final String title;
    private final String uri;

    ProblemType(String path, String title) {
        this.uri = "https://fintech.com.br" + path;
        this.title = title;
    }
}
