package com.gerador.pagamento.service;

import com.gerador.pagamento.DTO.ClienteDTO;
import com.gerador.pagamento.exception.ClienteException;
import com.gerador.pagamento.model.Cliente;
import com.gerador.pagamento.repository.ClienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public byte[] salvarCliente(ClienteDTO clienteDTO) {

        if (clienteDTO == null) {
            throw new ClienteException("Cliente não pode ser nulo");
        }

        Cliente cliente = new Cliente();

        BeanUtils.copyProperties(clienteDTO, cliente);
        byte[] pdfByte = GerarGuia.gerarGuiaPdf(
                cliente.getProprietario(),
                cliente.getCpf(),
                cliente.getEndereco(),
                new BigDecimal(String.valueOf(clienteDTO.getValor())),
                LocalDate.now().plusDays(5),
                "chave-pix-exemplo",
                cliente.getRecebedor().getNome(),
                cliente.getRecebedor().getCidade());

        if (pdfByte == null) {
            throw new ClienteException("Erro ao gerar pdf");
        }

        try {
            cliente.getRecebedor().setCliente(cliente);
            clienteRepository.save(cliente);
            return pdfByte;
        } catch (ConstraintViolationException e) {
            throw new ClienteException("Erro ao salvar o cliente: violação de constraint", e);
        } catch (DataAccessException e) {
            throw new ClienteException("Erro ao salvar o cliente no banco de dados", e);
        } catch (Exception e) {
            throw new ClienteException("Erro inesperado ao salvar o cliente", e);
        }
    }
}
