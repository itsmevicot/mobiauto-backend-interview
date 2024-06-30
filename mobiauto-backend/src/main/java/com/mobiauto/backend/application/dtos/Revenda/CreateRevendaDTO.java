package com.mobiauto.backend.application.dtos.Revenda;

import jakarta.validation.constraints.NotBlank;

public record CreateRevendaDTO(
        @NotBlank String cnpj,
        @NotBlank String codigo,
        @NotBlank String nomeSocial
) {}
