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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build();
            return verifier.verify(token);
        } catch (TokenExpiredException exception) {
            throw new RuntimeException("Sessão expirada. Faça login novamente.");
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Erro ao ler token. Faça login novamente.");
        }
    }

    public String generateLoginToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            List<String> roles = new ArrayList<>();
            roles.add("LOGIN_ONLY");

            if (usuario.getIsSuperuser()) {
                roles.add("ADMIN");
            }

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(usuario.getEmail())
                    .withClaim("roles", roles)
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token. Tente novamente.");
        }
    }

    public String generatePerfilToken(Usuario usuario, Perfil perfil) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            String perfilData = perfil != null ? perfil.getRevenda().getId() + "-" + perfil.getCargo().name() : "NONE";

            List<String> roles = usuario.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            if (usuario.getIsSuperuser()) {
                roles.add("ADMIN");
            }

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(usuario.getEmail())
                    .withClaim("roles", roles)
                    .withClaim("perfil", perfilData)
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token. Tente novamente.");
        }
    }

    private Instant genExpirationDate() {
        int expirationTime = 12;
        return LocalDateTime.now().plusHours(expirationTime).toInstant(ZoneOffset.of("-03:00"));
    }
}
