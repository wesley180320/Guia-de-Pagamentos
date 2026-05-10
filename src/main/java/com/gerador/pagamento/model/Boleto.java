package com.gerador.pagamento.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

}
