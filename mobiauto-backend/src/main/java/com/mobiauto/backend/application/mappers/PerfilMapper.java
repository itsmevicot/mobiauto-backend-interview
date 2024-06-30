package com.mobiauto.backend.application.mappers;

import com.mobiauto.backend.application.dtos.Perfil.PerfilDTO;
import com.mobiauto.backend.application.dtos.Perfil.CreatePerfilDTO;
import com.mobiauto.backend.application.dtos.Perfil.UpdatePerfilDTO;
import com.mobiauto.backend.domain.models.Perfil;
import com.mobiauto.backend.domain.models.Usuario;
import com.mobiauto.backend.domain.models.Revenda;
import com.mobiauto.backend.domain.models.Cargo;
import org.springframework.stereotype.Component;

@Component
public class PerfilMapper {

    public PerfilDTO toDTO(Perfil perfil) {
        return new PerfilDTO(
                perfil.getId(),
                perfil.getRevenda().getId(),
                perfil.getUsuario().getId(),
                perfil.getCargo().getId()
        );
    }

    public Perfil toEntity(CreatePerfilDTO dto, Usuario usuario, Revenda revenda, Cargo cargo) {
        Perfil perfil = new Perfil();
        perfil.setUsuario(usuario);
        perfil.setRevenda(revenda);
        perfil.setCargo(cargo);
        return perfil;
    }

    public void updateEntityFromDTO(UpdatePerfilDTO dto, Perfil perfil, Cargo cargo) {
        perfil.setCargo(cargo);
    }
}
