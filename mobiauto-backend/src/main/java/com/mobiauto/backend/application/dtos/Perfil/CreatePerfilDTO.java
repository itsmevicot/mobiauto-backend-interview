package com.mobiauto.backend.application.dtos.Perfil;

import jakarta.validation.constraints.NotNull;

public record CreatePerfilDTO(
        @NotNull(message = "Revenda ID é obrigatório.")
        Long revendaId,

        @NotNull(message = "Usuário ID é obrigatório.")
        Long usuarioId,

        @NotNull(message = "Cargo ID é obrigatório.")
        Long cargoId
) {}
