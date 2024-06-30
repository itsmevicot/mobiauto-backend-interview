package com.mobiauto.backend.application.dtos.Veiculo;

public record VeiculoDTO(
        Long id,
        String codigo,
        String marca,
        String modelo,
        String versao,
        int anoModelo,
        boolean ativo,
        Long revendaId
) {}
