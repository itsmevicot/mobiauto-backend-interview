package com.mobiauto.backend.application.dtos.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Email(message = "Digite um e-mail válido.")
        @NotBlank(message = "O campo e-mail é obrigatório")
        String email,

        @NotBlank(message = "O campo senha é obrigatório.")
        String senha
) {}
