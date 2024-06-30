package com.mobiauto.backend.application.mappers;

import com.mobiauto.backend.application.dtos.Usuario.UsuarioDTO;
import com.mobiauto.backend.application.dtos.Usuario.CreateUsuarioDTO;
import com.mobiauto.backend.application.dtos.Usuario.UpdateUsuarioDTO;
import com.mobiauto.backend.domain.models.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getCodigo(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.isAtivo()
        );
    }

    public Usuario toEntity(CreateUsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setCodigo(dto.codigo());
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());
        usuario.setAtivo(true);
        return usuario;
    }

    public void updateEntityFromDTO(UpdateUsuarioDTO dto, Usuario usuario) {
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());
    }
}
