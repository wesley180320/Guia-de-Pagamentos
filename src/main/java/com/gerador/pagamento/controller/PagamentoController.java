package com.gerador.pagamento.controller;

import com.gerador.pagamento.DTO.ClienteDTO;
import com.gerador.pagamento.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/pagamento")
public class PagamentoController {

    @Autowired
    private ClienteService clienteService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Object> save(@RequestBody @Valid ClienteDTO clienteDTO) throws Exception {
        clienteService.salvarCliente(clienteDTO);
        return ResponseEntity.ok().build();
    }

}
