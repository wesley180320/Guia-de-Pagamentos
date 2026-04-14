package com.gerador.pagamento.repository;

import com.gerador.pagamento.model.Boleto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoletoRepository extends JpaRepository<Boleto, Long> {
    @Query("SELECT b FROM Boleto b WHERE b.cliente.idCliente = :id")
    Page<Boleto> findByClienteIdCliente(@Param("id") Long id, Pageable pageable);
}
