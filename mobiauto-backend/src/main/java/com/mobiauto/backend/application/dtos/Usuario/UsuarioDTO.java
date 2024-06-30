package com.mobiauto.backend.application.dtos.Usuario;

public record UsuarioDTO(
        Long id,
        String codigo,
        String nome,
        String email,
        boolean ativo
) {}
