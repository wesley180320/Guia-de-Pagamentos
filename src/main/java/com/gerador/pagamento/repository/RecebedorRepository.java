package com.gerador.pagamento.repository;

import com.gerador.pagamento.model.Recebedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecebedorRepository extends JpaRepository<Recebedor, Long> {
}
