package com.mobiauto.backend.application.dtos.Perfil;

public record UpdatePerfilDTO(
        Long revendaId,
        Long usuarioId,
        Long cargoId
) {}
