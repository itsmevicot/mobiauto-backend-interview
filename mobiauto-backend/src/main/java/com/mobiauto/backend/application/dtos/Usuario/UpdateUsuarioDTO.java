package com.mobiauto.backend.application.dtos.Usuario;

public record UpdateUsuarioDTO(
        String codigo,
        String nome,
        String email,
        String senha
) {}
