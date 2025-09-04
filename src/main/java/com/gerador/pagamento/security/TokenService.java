package com.gerador.pagamento.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gerador.pagamento.model.Cliente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    DecodedJWT decodedJWT;

    public String gerarToken(Cliente cliente) {
        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            token = JWT.create().withIssuer("Agetec")
                    .withSubject(cliente.getCpf())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algorithm);
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
        return token;
    }

    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("Agetec").build();
            decodedJWT = verifier.verify(token.trim());
            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Erro ao validarToken", exception);
        }
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}