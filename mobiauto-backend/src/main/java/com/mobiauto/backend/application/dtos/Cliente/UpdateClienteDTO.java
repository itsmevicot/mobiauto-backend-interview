package com.mobiauto.backend.application.dtos.Cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateClienteDTO(
        @NotBlank(message = "O campo nome é obrigatório.") String nome,
        @Email(message = "Digite um e-mail válido.") @NotBlank(message = "O campo e-mail é obrigatório") String email,
        @NotBlank(message = "O campo telefone é obrigatório.") String telefone
) {}
