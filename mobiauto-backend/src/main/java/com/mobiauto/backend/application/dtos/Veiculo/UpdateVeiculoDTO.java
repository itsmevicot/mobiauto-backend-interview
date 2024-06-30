package com.mobiauto.backend.application.dtos.Veiculo;

public record UpdateVeiculoDTO(
        String codigo,
        String marca,
        String modelo,
        String versao,
        int anoModelo
) {}
