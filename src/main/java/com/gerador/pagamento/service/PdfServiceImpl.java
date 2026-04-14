package com.gerador.pagamento.service;

import com.gerador.pagamento.DTO.PagamentoDTO;
import com.gerador.pagamento.exception.ClienteException;
import com.gerador.pagamento.model.Boleto;
import com.gerador.pagamento.model.Cliente;
import com.gerador.pagamento.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PdfServiceImpl {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BoletoServiceImpl boletoServiceImpl;

    @Autowired
    private RecebedorServiceImpl recebedorServiceImpl;

    public byte[] gerarPdf(PagamentoDTO pagamentoDTO, Cliente cliente) {
        cliente = (Cliente) clienteRepository.findByCpf(cliente.getCpf());
        Boleto boleto = new Boleto();
        byte[] pdfByte = GerarGuiaImpl.gerarGuiaPdf(pagamentoDTO.getNome(), cliente.getCpf(), cliente.getEndereco(), new BigDecimal(String.valueOf(pagamentoDTO.getValor())), LocalDate.now().plusDays(5), "chave-pix-exemplo", pagamentoDTO.getNome(), pagamentoDTO.getCidade());
        if (pdfByte == null) {
            throw new ClienteException("Erro ao gerar pdf");
        }
        try {
            recebedorServiceImpl.salvarRecebedor(cliente, pagamentoDTO);
            boleto.setCliente(cliente);
            boletoServiceImpl.salvar(pdfByte,boleto);
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
