package com.mobiauto.backend.application.dtos.Permissao;

import jakarta.validation.constraints.NotBlank;

public record CreatePermissaoDTO(
        @NotBlank String descricao
) {}
