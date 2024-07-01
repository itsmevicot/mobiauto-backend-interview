package com.mobiauto.backend.application.dtos.Revenda;

import com.mobiauto.backend.domain.enums.StatusOportunidadeEnum;

public record RevendaDTO(
        Long id,
        String cnpj,
        String codigo,
        String nomeSocial,
        boolean ativo
) {}
