package com.mobiauto.backend.application.dtos.Cliente;

public record ClienteDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        boolean ativo
) {}
