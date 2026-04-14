package com.gerador.pagamento.service;

import com.gerador.pagamento.DTO.PagamentoDTO;
import com.gerador.pagamento.model.Cliente;
import com.gerador.pagamento.model.Recebedor;
import com.gerador.pagamento.repository.RecebedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecebedorServiceImpl {

    @Autowired
    private RecebedorRepository recebedorRepository;

    @Transactional
    public void salvarRecebedor (Cliente clienteLogado, PagamentoDTO pagamentoDTO){
        Recebedor recebedor = new Recebedor();
        recebedor.setCidade(pagamentoDTO.getCidade());
        recebedor.setNome(pagamentoDTO.getNome());
        recebedor.setValor(pagamentoDTO.getValor());
        recebedor.setCliente(clienteLogado);
        recebedor.setChavePix(pagamentoDTO.getChavePix());
        clienteLogado.setRecebedor(recebedor);
        recebedorRepository.save(recebedor);
    }

}
