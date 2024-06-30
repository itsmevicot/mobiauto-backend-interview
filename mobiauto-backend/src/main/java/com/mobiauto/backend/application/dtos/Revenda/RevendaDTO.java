package com.mobiauto.backend.application.dtos.Revenda;

import com.mobiauto.backend.domain.enums.StatusRevendaEnum;

public record RevendaDTO(
        Long id,
        String cnpj,
        String codigo,
        String nomeSocial,
        StatusRevendaEnum status,
        boolean ativo
) {}
