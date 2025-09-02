package com.gerador.pagamento.service;

import com.gerador.pagamento.DTO.ClienteDTO;
import com.gerador.pagamento.model.Cliente;
import com.gerador.pagamento.repository.ClienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public void salvarCliente(ClienteDTO clienteDTO) throws Exception {
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteDTO, cliente);

        GerarGuia.gerarGuiaPdf(
                cliente.getProprietario(),
                cliente.getCpf(),
                cliente.getEndereco(),
                new BigDecimal("150.00"),
                LocalDate.now().plusDays(5),
                "chave-pix-exemplo",
                cliente.getRecebedor().getNome(),
                cliente.getRecebedor().getCidade()
        );
        clienteRepository.save(cliente);
    }
}
