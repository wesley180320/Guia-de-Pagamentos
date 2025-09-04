package com.gerador.pagamento.controller;

import com.gerador.pagamento.DTO.ClienteDTO;
import com.gerador.pagamento.model.Cliente;
import com.gerador.pagamento.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/pagamento")
public class PagamentoController {
    @Autowired
    private ClienteService clienteService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<byte[]> save(@RequestBody @Valid ClienteDTO clienteDTO, @AuthenticationPrincipal Cliente cliente) throws Exception {
        byte[] pdfBytes = clienteService.gerarPdf(clienteDTO, cliente);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=guia.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}
