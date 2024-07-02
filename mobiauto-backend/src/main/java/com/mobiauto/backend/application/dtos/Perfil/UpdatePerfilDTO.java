package com.mobiauto.backend.application.dtos.Perfil;

import com.mobiauto.backend.domain.enums.CargosEnum;
import jakarta.validation.constraints.NotNull;

public record UpdatePerfilDTO(
        @NotNull(message = "Cargo é obrigatório.")
        CargosEnum cargo
) {}
