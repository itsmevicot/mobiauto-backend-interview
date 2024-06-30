package com.mobiauto.backend.application.dtos.Oportunidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateOportunidadeDTO(
        @NotBlank String codigo,
        @NotBlank String status,
        String motivoConclusao,
        LocalDateTime dataAtribuicao,
        LocalDateTime dataConclusao,
        @NotNull Long clienteId,
        @NotNull Long revendaId,
        @NotNull Long veiculoId,
        @NotNull Long responsavelAtendimentoId
) {}
