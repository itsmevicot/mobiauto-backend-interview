package com.mobiauto.backend.application.dtos.Cliente;

public record UpdateClienteDTO(
        String nome,
        String email,
        String telefone
) {}
