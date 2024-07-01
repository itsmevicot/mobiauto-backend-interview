package com.mobiauto.backend.domain.utils;

import com.mobiauto.backend.domain.models.Usuario;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public boolean hasAccessToRevenda(Usuario usuario, Long revendaId) {
        return usuario.getPerfis().stream()
                .anyMatch(perfil -> perfil.getRevenda().getId().equals(revendaId));
    }

    public boolean isOwnerOfOportunidade(Usuario usuario, Long oportunidadeId) {
        return usuario.getOportunidades().stream()
                .anyMatch(oportunidade -> oportunidade.getId().equals(oportunidadeId));
    }
}
