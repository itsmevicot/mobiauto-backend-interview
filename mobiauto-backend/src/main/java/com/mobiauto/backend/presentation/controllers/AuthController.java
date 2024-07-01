package com.mobiauto.backend.presentation.controllers;

import com.mobiauto.backend.application.dtos.Auth.LoginRequestDTO;
import com.mobiauto.backend.application.dtos.Auth.LoginResponseDTO;
import com.mobiauto.backend.application.dtos.Auth.PerfilSelectionDTO;
import com.mobiauto.backend.application.dtos.Usuario.CreateUsuarioDTO;
import com.mobiauto.backend.application.services.TokenService;
import com.mobiauto.backend.application.services.UsuarioService;
import com.mobiauto.backend.domain.models.Perfil;
import com.mobiauto.backend.domain.models.Usuario;
import com.mobiauto.backend.domain.repositories.PerfilRepository;
import com.mobiauto.backend.domain.repositories.UsuarioRepository;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AuthController(UsuarioService usuarioService, AuthenticationManager authenticationManager, TokenService tokenService, PerfilRepository perfilRepository, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody CreateUsuarioDTO createUsuarioDTO) {
        return ResponseEntity.ok(usuarioService.createUsuario(createUsuarioDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        logger.info("Login attempt for email: {}", loginRequestDTO.email());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.senha())
            );

            Usuario usuario = (Usuario) authentication.getPrincipal();
            String token = tokenService.generateLoginToken(usuario);
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for email: {}", loginRequestDTO.email(), e);
            return ResponseEntity.status(401).body(new LoginResponseDTO("Authentication failed: " + e.getMessage()));
        }
    }

    @PostMapping("/perfil")
    public ResponseEntity<LoginResponseDTO> selectPerfil(@RequestBody PerfilSelectionDTO perfilSelectionDTO, @RequestHeader("Authorization") String token) {
        try {
            DecodedJWT decodedJWT = tokenService.validateToken(token.replace("Bearer ", ""));
            String email = decodedJWT.getSubject();
            Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

            if (usuario.isEmpty()) {
                return ResponseEntity.status(404).body(new LoginResponseDTO("Usuario not found"));
            }

            Optional<Perfil> perfil = perfilRepository.findById(perfilSelectionDTO.perfilId());

            if (perfil.isEmpty() || !perfil.get().getUsuario().getId().equals(usuario.get().getId())) {
                return ResponseEntity.status(404).body(new LoginResponseDTO("Perfil not found or does not belong to the user"));
            }

            String perfilToken = tokenService.generatePerfilToken(usuario.get(), perfil.get());
            return ResponseEntity.ok(new LoginResponseDTO(perfilToken));
        } catch (Exception e) {
            logger.error("Error selecting perfil for token: {}", token, e);
            return ResponseEntity.status(401).body(new LoginResponseDTO("Error selecting perfil: " + e.getMessage()));
        }
    }
}
