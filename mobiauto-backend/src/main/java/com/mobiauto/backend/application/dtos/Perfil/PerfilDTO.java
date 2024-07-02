package com.mobiauto.backend.application.dtos.Perfil;

public record PerfilDTO(
        Long id,
        Long revendaId,
        Long usuarioId,
        String cargo
) {}
