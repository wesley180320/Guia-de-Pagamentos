package com.gerador.pagamento.DTO;

import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;

public class LoginDTO {
    @CPF(message = "CPF inválido")
    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "A senha é obrigatório")
    private String senha;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
