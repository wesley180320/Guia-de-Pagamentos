package com.gerador.pagamento.service;

import com.gerador.pagamento.exception.ClienteException;
import com.gerador.pagamento.model.Boleto;
import com.gerador.pagamento.model.Cliente;
import com.gerador.pagamento.repository.BoletoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoletoServiceImpl implements BoletoService {

    @Autowired
    private BoletoRepository boletoRepository;

    @Override
    public Page<Boleto> buscaPaginadaPorId(Long idCliente, Pageable pageable) {
        return boletoRepository.findByClienteIdCliente(idCliente, pageable);
    }

    @Transactional
    @Override
    public void salvar(Boleto boleto) {
        if (boleto.getBoletoByte() == null) {
            throw new ClienteException("Erro pdfBoleto nullo");
        }
        boletoRepository.save(boleto);
    }

    @Override
    public void deletar(Boleto boleto) {
    }


}
