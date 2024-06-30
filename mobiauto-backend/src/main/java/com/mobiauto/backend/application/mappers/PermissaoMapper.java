package com.mobiauto.backend.application.mappers;

import com.mobiauto.backend.application.dtos.Permissao.PermissaoDTO;
import com.mobiauto.backend.application.dtos.Permissao.CreatePermissaoDTO;
import com.mobiauto.backend.application.dtos.Permissao.UpdatePermissaoDTO;
import com.mobiauto.backend.domain.models.Permissao;
import org.springframework.stereotype.Component;

@Component
public class PermissaoMapper {

    public PermissaoDTO toDTO(Permissao permissao) {
        return new PermissaoDTO(
                permissao.getId(),
                permissao.getDescricao()
        );
    }

    public Permissao toEntity(CreatePermissaoDTO dto) {
        Permissao permissao = new Permissao();
        permissao.setDescricao(dto.descricao());
        return permissao;
    }

    public void updateEntityFromDTO(UpdatePermissaoDTO dto, Permissao permissao) {
        permissao.setDescricao(dto.descricao());
    }
}
