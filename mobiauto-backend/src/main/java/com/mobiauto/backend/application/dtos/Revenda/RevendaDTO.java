package com.mobiauto.backend.application.dtos.Revenda;

public record RevendaDTO(
        Long id,
        String cnpj,
        String codigo,
        String nomeSocial,
        boolean ativo
) {}
