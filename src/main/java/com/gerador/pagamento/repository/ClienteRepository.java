package com.gerador.pagamento.repository;

import com.gerador.pagamento.model.Cliente;
import org.hibernate.annotations.Nationalized;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    UserDetails findByCpf(String cpf);

    @Query("SELECT c FROM Cliente c JOIN FETCH c.recebedor WHERE c.cpf = :cpf")
    Cliente findByClienteComRecebedor(String cpf);
}
