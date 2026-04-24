package com.gerador.pagamento.controller;

import com.gerador.pagamento.model.Boleto;
import com.gerador.pagamento.service.BoletoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "boleto")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BoletoController {

    private final BoletoService boletoService;

    public BoletoController(BoletoService boletoService) {
        this.boletoService = boletoService;
    }

    @GetMapping("/consulta/")
    public ResponseEntity<Page<Boleto>> buscarBoletoPorId(
            @RequestParam Long idCliente,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<Boleto> page = boletoService.buscaPaginadaPorId(idCliente, pageable);
        return ResponseEntity.ok(page);
    }

}
