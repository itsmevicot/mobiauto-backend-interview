package com.mobiauto.backend.application.dtos.Veiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateVeiculoDTO(
        @NotBlank String codigo,
        @NotBlank String marca,
        @NotBlank String modelo,
        @NotBlank String versao,
        @NotNull int anoModelo
) {}
