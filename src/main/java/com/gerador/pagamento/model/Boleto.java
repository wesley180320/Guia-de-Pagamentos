package com.gerador.pagamento.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Boleto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_boleto", nullable = false)
    private Long idBoleto;
    private Date dataCriacao = new Date();
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    @Lob
    @Column(name = "boleto", columnDefinition = "BLOB")
    private byte[] boletoByte;

    public Boleto() {
    }

    public Boleto(Long idBoleto, Date dataCriacao, Cliente cliente, byte[] boletoByte) {
        this.idBoleto = idBoleto;
        this.dataCriacao = dataCriacao;
        this.cliente = cliente;
        this.boletoByte = boletoByte;
    }

    public Long getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(Long idBoleto) {
        this.idBoleto = idBoleto;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public byte[] getBoletoByte() {
        return boletoByte;
    }

    public void setBoletoByte(byte[] boletoByte) {
        this.boletoByte = boletoByte;
    }
}
