package com.gerador.pagamento.service;

import com.gerador.pagamento.DTO.PagamentoDTO;
import com.gerador.pagamento.model.Cliente;
import com.gerador.pagamento.model.Recebedor;

public interface RecebedorService {
    void salvarRecebedor(Cliente clienteLogado, PagamentoDTO pagamentoDTO);
    Recebedor criarRecebedor(PagamentoDTO pagamentoDTO, Cliente clienteLogado);
}
