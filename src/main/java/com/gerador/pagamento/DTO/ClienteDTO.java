package com.gerador.pagamento.DTO;

import com.gerador.pagamento.model.Recebedor;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public class ClienteDTO {
    private String proprietario;
    @CPF(message = "CPF inválido")
    private String cpf;
    private String endereco;
    private BigDecimal valor;
    private Recebedor recebedor;


    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Recebedor getRecebedor() {
        return recebedor;
    }

    public void setRecebedor(Recebedor recebedor) {
        this.recebedor = recebedor;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
