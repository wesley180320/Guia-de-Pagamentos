package com.gerador.pagamento.security;

import com.gerador.pagamento.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationService authenticationService;

    public CustomAuthenticationProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String cpfCliente = authentication.getName();
        String password = authentication.getCredentials().toString();
        return authenticationService.validaClienteESenha(cpfCliente, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
