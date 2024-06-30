package com.mobiauto.backend.application.dtos.Cargo;

import jakarta.validation.constraints.NotBlank;

public record CreateCargoDTO(
        @NotBlank String nome
) {}
