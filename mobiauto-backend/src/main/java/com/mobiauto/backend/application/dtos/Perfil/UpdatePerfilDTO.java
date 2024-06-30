package com.mobiauto.backend.application.dtos.Perfil;

import jakarta.validation.constraints.NotNull;

public record UpdatePerfilDTO(
        @NotNull(message = "Cargo ID é obrigatório.")
        Long cargoId
) {}
