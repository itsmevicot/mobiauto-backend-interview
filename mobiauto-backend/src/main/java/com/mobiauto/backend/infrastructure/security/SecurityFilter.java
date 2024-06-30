package com.mobiauto.backend.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mobiauto.backend.application.services.TokenService;
import com.mobiauto.backend.domain.exceptions.Usuario.UsuarioNotFoundException;
import com.mobiauto.backend.domain.models.HttpResponse;
import com.mobiauto.backend.domain.models.Usuario;
import com.mobiauto.backend.domain.repositories.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            var token = this.recoverToken(request);
            if (token != null) {
                var userJson = tokenService.validateToken(token);
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> mapUser = mapper.readValue(userJson, Map.class);
                String email = mapUser.get("sub").toString();
                Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

                if (usuario.isEmpty()) throw new UsuarioNotFoundException();

                var authentication = new UsernamePasswordAuthenticationToken(usuario.get(), null, usuario.get().getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);

        } catch (IOException e) {
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, e);
        } catch (Exception e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        List<String> publicPaths = List.of("/api/v1/auth/login", "/api/v1/auth/register");
        return publicPaths.contains(path);
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
