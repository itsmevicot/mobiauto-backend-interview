package com.mobiauto.backend.application.dtos.Perfil;

import com.mobiauto.backend.domain.enums.CargosEnum;
import jakarta.validation.constraints.NotNull;

public record CreatePerfilDTO(
        @NotNull(message = "Revenda ID é obrigatório.")
        Long revendaId,

        @NotNull(message = "Usuário ID é obrigatório.")
        Long usuarioId,

        @NotNull(message = "Cargo é obrigatório.")
        CargosEnum cargo
) {}
