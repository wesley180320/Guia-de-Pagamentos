package com.gerador.pagamento.service;
import com.gerador.pagamento.model.Boleto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoletoService {
    void salvar(Boleto boleto);
    void deletar(Boleto boleto);
    Page<Boleto> buscaPaginadaPorId(Long id, Pageable pageable);
}
