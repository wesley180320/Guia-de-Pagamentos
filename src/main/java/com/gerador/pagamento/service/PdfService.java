package com.gerador.pagamento.service;

import com.gerador.pagamento.DTO.PagamentoDTO;
import com.gerador.pagamento.exception.ClienteException;
import com.gerador.pagamento.model.Cliente;
import com.gerador.pagamento.model.Recebedor;
import com.gerador.pagamento.repository.ClienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PdfService {

    @Autowired
    private ClienteRepository clienteRepository;

    public byte[] gerarPdf(PagamentoDTO pagamentoDTO, Cliente cliente) {

        Cliente clienteLogado = validarClienteLogado(cliente);
        Recebedor recebedor = new Recebedor();
        BeanUtils.copyProperties(pagamentoDTO, recebedor);
        clienteLogado.getRecebedor().setCidade(recebedor.getCidade());
        clienteLogado.getRecebedor().setNome(recebedor.getNome());

        byte[] pdfByte = GerarGuia.gerarGuiaPdf(recebedor.getNome(), clienteLogado.getCpf(), clienteLogado.getEndereco(), new BigDecimal(String.valueOf(recebedor.getValor())), LocalDate.now().plusDays(5), "chave-pix-exemplo", clienteLogado.getRecebedor().getNome(), clienteLogado.getRecebedor().getCidade());

        if (pdfByte == null) {
            throw new ClienteException("Erro ao gerar pdf");
        }

        try {
            clienteLogado.getRecebedor().setCliente(clienteLogado);
            clienteRepository.save(clienteLogado);
            return pdfByte;
        } catch (ConstraintViolationException e) {
            throw new ClienteException("Erro ao salvar o cliente: violação de constraint", e);
        } catch (DataAccessException e) {
            throw new ClienteException("Erro ao salvar o cliente no banco de dados", e);
        } catch (Exception e) {
            throw new ClienteException("Erro inesperado ao salvar o cliente", e);
        }
    }

    private Cliente validarClienteLogado(Cliente cliente) {
        Cliente clienteLogado = clienteRepository.findByClienteComRecebedor(cliente.getCpf());
        if (clienteLogado == null) {
            throw new ClienteException("Cliente não pode ser nulo");
        }
        return clienteLogado;
    }
}
