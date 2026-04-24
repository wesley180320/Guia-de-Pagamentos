package com.gerador.pagamento.service;

import com.gerador.pagamento.exception.ClienteException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService {

    private final ClienteService clienteService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(ClienteService clienteService, PasswordEncoder passwordEncoder) {
        this.clienteService = clienteService;
        this.passwordEncoder = passwordEncoder;
    }

    public Authentication validaClienteESenha(String cpfCliente, String password) throws AuthenticationException {
        UserDetails usuario = clienteService.validaCpf(cpfCliente);
        if (usuario == null) {
            throw new ClienteException("Cliente não encontrado");
        }
        if (passwordEncoder.matches(password, usuario.getPassword())) {
            return new UsernamePasswordAuthenticationToken(usuario, null, new ArrayList<>());
        }
        throw new BadCredentialsException("Senha incorreta");
    }
}
