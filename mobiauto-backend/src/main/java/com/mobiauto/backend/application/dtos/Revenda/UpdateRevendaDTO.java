package com.mobiauto.backend.application.dtos.Revenda;

public record UpdateRevendaDTO(
        String cnpj,
        String codigo,
        String nomeSocial
) {}
