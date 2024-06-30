package com.mobiauto.backend.application.dtos.Oportunidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateOportunidadeDTO(
        @NotBlank(message = "O código é obrigatório.")
        String codigo,

        @NotBlank(message = "O status é obrigatório.")
        String status,

        String motivoConclusao,

        LocalDateTime dataAtribuicao,

        LocalDateTime dataConclusao,

        @NotNull(message = "Cliente ID é obrigatório.")
        Long clienteId,

        @NotNull(message = "Revenda ID é obrigatório.")
        Long revendaId,

        @NotNull(message = "Veículo ID é obrigatório.")
        Long veiculoId,

        @NotNull(message = "Responsável pelo atendimento ID é obrigatório.")
        Long responsavelAtendimentoId
) {}
