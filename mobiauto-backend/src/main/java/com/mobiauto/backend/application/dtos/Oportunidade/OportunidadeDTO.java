package com.mobiauto.backend.application.dtos.Oportunidade;

import java.time.LocalDateTime;

public record OportunidadeDTO(
        Long id,
        String codigo,
        String status,
        String motivoConclusao,
        LocalDateTime dataAtribuicao,
        LocalDateTime dataConclusao,
        Long clienteId,
        Long revendaId,
        Long veiculoId,
        Long responsavelAtendimentoId
) {}
