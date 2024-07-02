package com.mobiauto.backend.application.dtos.Perfil;

import com.mobiauto.backend.domain.enums.CargosEnum;

public record CurrentPerfilDTO(Long revendaId, CargosEnum cargoNome) {}

