package com.gerador.pagamento.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_cliente", nullable = false)
    private UUID idCliente;
    private String proprietario;
    private String cpf;
    private String endereco;
    private BigDecimal valor;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Recebedor recebedor;

    public Cliente(){}

    public Cliente(UUID idCliente, String proprietario, String cpf, String endereco, BigDecimal valor) {
        this.idCliente = idCliente;
        this.proprietario = proprietario;
        this.cpf = cpf;
        this.endereco = endereco;
        this.valor = valor;
    }

    public UUID getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(UUID idCliente) {
        this.idCliente = idCliente;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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
}
