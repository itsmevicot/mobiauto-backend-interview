package com.mobiauto.backend.application.dtos.Cliente;

import jakarta.validation.constraints.NotBlank;

public record CreateClienteDTO(
        @NotBlank String nome,
        @NotBlank String email,
        @NotBlank String telefone
) {}
