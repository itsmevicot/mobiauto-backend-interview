package com.mobiauto.backend.application.mappers;

import com.mobiauto.backend.application.dtos.Revenda.RevendaDTO;
import com.mobiauto.backend.application.dtos.Revenda.CreateRevendaDTO;
import com.mobiauto.backend.application.dtos.Revenda.UpdateRevendaDTO;
import com.mobiauto.backend.domain.models.Revenda;
import org.springframework.stereotype.Component;

@Component
public class RevendaMapper {

    public RevendaDTO toDTO(Revenda revenda) {
        return new RevendaDTO(
                revenda.getId(),
                revenda.getCnpj(),
                revenda.getCodigo(),
                revenda.getNomeSocial(),
                revenda.isAtivo()
        );
    }

    public Revenda toEntity(CreateRevendaDTO dto) {
        Revenda revenda = new Revenda();
        revenda.setCnpj(dto.cnpj());
        revenda.setNomeSocial(dto.nomeSocial());
        revenda.setAtivo(dto.ativo());
        return revenda;
    }

    public void updateEntityFromDTO(UpdateRevendaDTO dto, Revenda revenda) {
        revenda.setCnpj(dto.cnpj());
        revenda.setNomeSocial(dto.nomeSocial());
        revenda.setAtivo(dto.ativo());
    }
}
