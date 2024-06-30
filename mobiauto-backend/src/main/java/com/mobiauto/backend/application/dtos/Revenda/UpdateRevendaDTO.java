package com.mobiauto.backend.application.dtos.Revenda;

import com.mobiauto.backend.domain.enums.StatusRevendaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRevendaDTO(
        @NotBlank(message = "O CNPJ é obrigatório.")
        String cnpj,

        @NotBlank(message = "O código é obrigatório.")
        String codigo,

        @NotBlank(message = "O nome social é obrigatório.")
        String nomeSocial,

        @NotNull(message = "O status é obrigatório.")
        StatusRevendaEnum status,

        @NotNull(message = "O status ativo é obrigatório.")
        boolean ativo
) {}
