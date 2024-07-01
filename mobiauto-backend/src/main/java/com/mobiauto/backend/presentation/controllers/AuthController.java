package com.mobiauto.backend.presentation.controllers;

import com.mobiauto.backend.application.dtos.Auth.LoginRequestDTO;
import com.mobiauto.backend.application.dtos.Auth.LoginResponseDTO;
import com.mobiauto.backend.application.dtos.Usuario.CreateUsuarioDTO;
import com.mobiauto.backend.application.services.TokenService;
import com.mobiauto.backend.application.services.UsuarioService;
import com.mobiauto.backend.domain.models.Perfil;
import com.mobiauto.backend.domain.models.Usuario;
import com.mobiauto.backend.domain.repositories.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PerfilRepository perfilRepository;

    @Autowired
    public AuthController(UsuarioService usuarioService, AuthenticationManager authenticationManager, TokenService tokenService, PerfilRepository perfilRepository) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.perfilRepository = perfilRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody CreateUsuarioDTO createUsuarioDTO) {
        return ResponseEntity.ok(usuarioService.createUsuario(createUsuarioDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.senha())
            );

            Usuario usuario = (Usuario) authentication.getPrincipal();
            Perfil perfil = perfilRepository.findByUsuarioId(usuario.getId()).orElse(null);

            String token = tokenService.generateToken(usuario, perfil);
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }

    public static class AuthResponse {
        private String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
