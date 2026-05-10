package com.gerador.pagamento.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recebedor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_recebedor", nullable = false)
    private Long idRecebedor;
    private String chavePix;
    private String nome;
    private String cidade;
    private Long  valor;

    @OneToOne(mappedBy = "recebedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cliente cliente;

}
