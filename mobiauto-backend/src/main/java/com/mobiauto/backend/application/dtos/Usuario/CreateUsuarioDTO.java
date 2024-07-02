package com.mobiauto.backend.application.dtos.Usuario;

import jakarta.validation.constraints.NotBlank;

public record CreateUsuarioDTO(
        @NotBlank String nome,
        @NotBlank String email,
        @NotBlank String senha
) {}
