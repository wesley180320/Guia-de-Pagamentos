package com.gerador.pagamento.service;

import com.gerador.pagamento.model.Boleto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoletoService<T> {

    void salvarBoleto(byte[] pdfByte, T entidade);
    void deletar(Long id);
    Page<Boleto> buscaBoletoPorIdCliente(Long idCliente, Pageable pageable);
}
