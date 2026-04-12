package com.gerador.pagamento.controller;

import com.gerador.pagamento.DTO.PagamentoDTO;
import com.gerador.pagamento.model.Cliente;
import com.gerador.pagamento.service.PdfService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/pagamento")
public class PagamentoController {
    @Autowired
    private PdfService pdfService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<byte[]> save(@RequestBody @Valid PagamentoDTO pagamentoDTO, @Parameter(hidden = true)@AuthenticationPrincipal Cliente cliente) throws Exception {
        byte[] pdfBytes = pdfService.gerarPdf(pagamentoDTO, cliente);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=guia.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}
