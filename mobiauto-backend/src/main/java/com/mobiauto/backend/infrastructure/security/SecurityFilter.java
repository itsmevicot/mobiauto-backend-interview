package com.mobiauto.backend.infrastructure.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mobiauto.backend.application.services.TokenService;
import com.mobiauto.backend.domain.exceptions.Usuario.UsuarioNotFoundException;
import com.mobiauto.backend.domain.models.HttpResponse;
import com.mobiauto.backend.domain.models.Usuario;
import com.mobiauto.backend.domain.repositories.UsuarioRepository;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = recoverToken(request);
        if (token != null) {
            try {
                DecodedJWT decodedJWT = tokenService.validateToken(token);
                String email = decodedJWT.getSubject();
                List<GrantedAuthority> authorities = decodedJWT.getClaim("roles").asList(String.class).stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, token, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);


            } catch (JWTVerificationException exception) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido ou expirado.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        List<String> publicPaths = List.of(
                "/api/v1/auth/login",
                "/api/v1/auth/register",
                "/swagger-ui/",
                "/v3/api-docs",
                "/swagger-ui.html",
                "/v3/api-docs.yaml",
                "/v3/api-docs/**"
        );
        return publicPaths.stream().anyMatch(path::startsWith);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer", "").trim();
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable e) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpResponse httpResponse = HttpResponse.builder()
                .timestamp(java.time.LocalDateTime.now().toString())
                .statusCode(status.value())
                .status(status)
                .message(e.getMessage())
                .build();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(httpResponse);
        response.getWriter().write(json);
    }
}
