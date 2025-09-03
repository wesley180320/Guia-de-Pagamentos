package com.gerador.pagamento.DTO;

import com.gerador.pagamento.model.Recebedor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ClienteDTO {

    @NotBlank(message = "Proprietário é obrigatório")
    private String proprietario;
    @CPF(message = "CPF inválido")
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;
    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;
    @NotNull(message = "Valor é obrigatório")
    private BigDecimal valor;
    @NotNull(message = "Recebedor é obrigatório")
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
