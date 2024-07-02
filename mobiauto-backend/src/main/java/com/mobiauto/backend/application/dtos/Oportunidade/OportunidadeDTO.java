package com.mobiauto.backend.application.dtos.Oportunidade;

import com.mobiauto.backend.domain.enums.StatusOportunidadeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public record OportunidadeDTO(
        Long id,
        String codigo,
        StatusOportunidadeEnum status,
        String motivoConclusao,
        LocalDateTime dataAtribuicao,
        LocalDateTime dataConclusao,
        Long clienteId,
        Long revendaId,
        Long veiculoId,
        Long responsavelAtendimentoId
) implements Serializable {}
