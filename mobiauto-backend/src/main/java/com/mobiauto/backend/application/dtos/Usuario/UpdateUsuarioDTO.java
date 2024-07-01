package com.mobiauto.backend.application.dtos.Usuario;

public record UpdateUsuarioDTO(
        String nome,
        String email,
        String senha
) {}
