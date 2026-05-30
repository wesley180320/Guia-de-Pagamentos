package com.gerador.pagamento.service;

import com.gerador.pagamento.DTO.PagamentoDTO;
import com.gerador.pagamento.model.Cliente;

public interface PdfService {

    byte[] gerarPdf(PagamentoDTO pagamentoDTO, String cpfCliente);
    void validarPdf(byte[] pdfByte);
    void salvarRecebedor(Cliente cliente, PagamentoDTO pagamentoDTO);
    void salvarBoleto(Cliente cliente, byte[] pdfByte);
}
