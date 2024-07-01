package com.mobiauto.backend.application.dtos.Revenda;

import com.mobiauto.backend.domain.enums.StatusOportunidadeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;

public record UpdateRevendaDTO(
        @CNPJ
        @NotBlank(message = "O CNPJ é obrigatório.")
        String cnpj,

        @NotBlank(message = "O nome social é obrigatório.")
        String nomeSocial,

        @NotNull(message = "O status é obrigatório.")
        StatusOportunidadeEnum status,

        @NotNull(message = "O status ativo é obrigatório.")
        boolean ativo
) {}
