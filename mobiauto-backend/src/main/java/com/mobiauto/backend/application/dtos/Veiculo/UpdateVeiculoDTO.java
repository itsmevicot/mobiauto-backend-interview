package com.mobiauto.backend.application.dtos.Veiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateVeiculoDTO(
        @NotBlank(message = "O código é obrigatório.")
        String codigo,

        @NotBlank(message = "A marca é obrigatória.")
        String marca,

        @NotBlank(message = "O modelo é obrigatório.")
        String modelo,

        @NotBlank(message = "A versão é obrigatória.")
        String versao,

        @NotNull(message = "O ano do modelo é obrigatório.")
        int anoModelo,

        @NotNull(message = "O status ativo é obrigatório.")
        boolean ativo,

        @NotNull(message = "Revenda ID é obrigatório.")
        Long revendaId
) {}
