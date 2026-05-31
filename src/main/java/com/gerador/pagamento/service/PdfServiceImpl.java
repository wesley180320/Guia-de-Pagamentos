package com.gerador.pagamento.service;

import com.gerador.pagamento.DTO.PagamentoDTO;
import com.gerador.pagamento.exception.ClienteException;
import com.gerador.pagamento.model.Boleto;
import com.gerador.pagamento.model.Cliente;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PdfServiceImpl implements PdfService{

    private final ClienteService clienteService;
    private final BoletoService boletoService;
    private final RecebedorService recebedorService;

    public PdfServiceImpl(ClienteService clienteService,
                          BoletoService boletoService,
                          RecebedorService recebedorService) {
        this.clienteService = clienteService;
        this.boletoService = boletoService;
        this.recebedorService = recebedorService;
    }

    public byte[] gerarPdf(PagamentoDTO pagamentoDTO, String cpfCliente) {
        Cliente clienteValidado = clienteService.validaCpf(cpfCliente);
        byte[] pdfByte = GerarGuiaImpl.gerarGuiaPdf(pagamentoDTO.getNome(), clienteValidado.getCpf(), clienteValidado.getEndereco(), new BigDecimal(String.valueOf(pagamentoDTO.getValor())), LocalDate.now().plusDays(5), "chave-pix-exemplo", pagamentoDTO.getNome(), pagamentoDTO.getCidade());
        validarPdf(pdfByte);
        salvarRecebedor(clienteValidado, pagamentoDTO);
        salvarBoleto(clienteValidado, pdfByte);
        return pdfByte;
    }

    public void validarPdf(byte[] pdfByte) {
        if (pdfByte == null) {
            throw new ClienteException("Erro ao gerar pdf");
        }
    }

    public void salvarRecebedor(Cliente cliente, PagamentoDTO pagamentoDTO) {
        recebedorService.salvarRecebedor(cliente, pagamentoDTO);
    }

    public void salvarBoleto(Cliente cliente, byte[] pdfByte) {
        boletoService.salvar(new Boleto(null, null, cliente, pdfByte));
    }
}
