package com.mobiauto.backend.application.dtos.Oportunidade;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateOportunidadeDTO(
        String motivoConclusao,

        LocalDateTime dataAtribuicao,

        LocalDateTime dataConclusao,

        @NotNull(message = "Cliente ID é obrigatório.")
        Long clienteId,

        @NotNull(message = "Revenda ID é obrigatório.")
        Long revendaId,

        @NotNull(message = "Veículo ID é obrigatório.")
        Long veiculoId
) {}
