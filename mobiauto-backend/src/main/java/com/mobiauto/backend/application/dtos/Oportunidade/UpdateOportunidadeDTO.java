package com.mobiauto.backend.application.dtos.Oportunidade;

import com.mobiauto.backend.domain.enums.StatusOportunidadeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record UpdateOportunidadeDTO(
        @NotBlank(message = "O status é obrigatório.")
        StatusOportunidadeEnum status,

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
