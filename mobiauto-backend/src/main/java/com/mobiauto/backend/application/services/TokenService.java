package com.mobiauto.backend.application.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mobiauto.backend.domain.models.Perfil;
import com.mobiauto.backend.domain.models.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getPayload();
        } catch (TokenExpiredException exception) {
            throw new RuntimeException("Sessão expirada. Faça login novamente.");
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Erro ao ler token. Faça login novamente.");
        }
    }

    public String generateToken(Usuario usuario, Perfil perfil) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            String perfilData = perfil != null ? perfil.getRevenda().getCodigo() + "-" + perfil.getCargo().getNome() : "NONE";
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(usuario.getEmail())
                    .withClaim("roles", usuario.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList()))
                    .withClaim("perfil", perfilData)
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token. Tente novamente.");
        }
    }

    private Instant genExpirationDate() {
        int expirationTime = 12;
        return LocalDateTime.now().plusHours(expirationTime).toInstant(ZoneOffset.of("-03:00"));
    }
}
