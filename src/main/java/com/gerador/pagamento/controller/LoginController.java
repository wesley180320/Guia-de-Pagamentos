package com.gerador.pagamento.controller;

import com.gerador.pagamento.DTO.ClienteDTO;
import com.gerador.pagamento.DTO.LoginDTO;
import com.gerador.pagamento.DTO.LoginResponseDTO;
import com.gerador.pagamento.model.Cliente;
import com.gerador.pagamento.security.TokenService;
import com.gerador.pagamento.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LoginController {
    @Autowired
    ClienteService clienteService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TokenService tokenService;

    @RequestMapping(value = "/cadastro", method = RequestMethod.POST)
    private ResponseEntity<Object> save(@RequestBody @Valid LoginDTO loginDTO, BindingResult result) {
        clienteService.salvarCliente(loginDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    private ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.getCpf(), loginDTO.getSenha());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.gerarToken((Cliente) auth.getPrincipal());
        return ResponseEntity.ok().body(new LoginResponseDTO(token));
    }
}
