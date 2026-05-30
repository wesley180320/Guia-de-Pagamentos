package com.gerador.pagamento.enums;

public enum MetodoHttp {

    GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE"), OPTIONS("OPTIONS");

    private final String nome;

    MetodoHttp(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
