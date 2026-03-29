package com.gerador.pagamento.DTO;

import com.gerador.pagamento.model.Recebedor;

public class PagamentoDTO {

    private String chavePix;
    private String nome;
    private String cidade;
    private Long valor;

    public PagamentoDTO() {
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }
}
