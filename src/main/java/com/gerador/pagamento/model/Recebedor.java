package com.gerador.pagamento.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Recebedor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_recebedor", nullable = false)
    private UUID idRecebedor;
    private String chavePix;
    private String nome;
    private String cidade;
    @OneToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id_cliente")
    private Cliente cliente;

    public Recebedor(){}

    public Recebedor(UUID idRecebedor, String chavePix, String nome, String cidade) {
        this.idRecebedor = idRecebedor;
        this.chavePix = chavePix;
        this.nome = nome;
        this.cidade = cidade;
    }

    public UUID getIdRecebedor() {
        return idRecebedor;
    }

    public void setIdRecebedor(UUID idRecebedor) {
        this.idRecebedor = idRecebedor;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
