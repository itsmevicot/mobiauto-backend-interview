package com.mobiauto.backend.application.dtos.Perfil;

import jakarta.validation.constraints.NotNull;

public record CreatePerfilDTO(
        @NotNull Long revendaId,
        @NotNull Long usuarioId,
        @NotNull Long cargoId
) {}
