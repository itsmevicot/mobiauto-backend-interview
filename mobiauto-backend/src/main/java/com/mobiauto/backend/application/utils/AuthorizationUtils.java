package com.mobiauto.backend.application.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.mobiauto.backend.application.dtos.Perfil.CurrentPerfilDTO;
import com.mobiauto.backend.domain.enums.CargosEnum;
import com.mobiauto.backend.domain.exceptions.Auth.UnauthorizedException;
import com.mobiauto.backend.domain.models.Usuario;
import com.mobiauto.backend.application.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorizationUtils {

    @Autowired
    private TokenService tokenService;

    public boolean isSuperuser(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    }

    public boolean canAssignRole(Usuario usuario, CargosEnum newCargo) {
        List<CargosEnum> assignableRoles = List.of(CargosEnum.values());

        if (isSuperuser(SecurityContextHolder.getContext().getAuthentication())) {
            return true;
        }

        CurrentPerfilDTO currentPerfil = getCurrentPerfil();

        return assignableRoles.indexOf(newCargo) <= assignableRoles.indexOf(currentPerfil.cargoNome());
    }

    public boolean hasRole(CargosEnum role) {
        CurrentPerfilDTO currentPerfil = getCurrentPerfil();
        return currentPerfil.cargoNome() == role;
    }

    public boolean hasAccessToRevenda(Long revendaId) {
        CurrentPerfilDTO currentPerfil = getCurrentPerfil();
        return currentPerfil.revendaId().equals(revendaId);
    }

    public boolean isAuthorizedToCreatePerfil(Long revendaId, CargosEnum newCargo) {
        return isSuperuser(SecurityContextHolder.getContext().getAuthentication()) ||
                hasRole(CargosEnum.PROPRIETARIO) ||
                (hasRole(CargosEnum.GERENTE) && newCargo == CargosEnum.ASSISTENTE);
    }

    public boolean isOwnerOfOportunidade(Usuario usuario, Long oportunidadeId) {
        return usuario.getOportunidades().stream()
                .anyMatch(oportunidade -> oportunidade.getId().equals(oportunidadeId));
    }

    public boolean canTransferOportunidade() {
        return isSuperuser(SecurityContextHolder.getContext().getAuthentication()) ||
                hasRole(CargosEnum.PROPRIETARIO) ||
                hasRole(CargosEnum.GERENTE);
    }

    public boolean canEditOportunidade(Usuario usuario, Long oportunidadeId) {
        return isSuperuser(SecurityContextHolder.getContext().getAuthentication()) ||
                isOwnerOfOportunidade(usuario, oportunidadeId) ||
                hasRole(CargosEnum.PROPRIETARIO) ||
                hasRole(CargosEnum.GERENTE);
    }

    public CurrentPerfilDTO getCurrentPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getCredentials() == null) {
            throw new UnauthorizedException();
        }

        String token = authentication.getCredentials().toString();

        DecodedJWT decodedJWT = tokenService.validateToken(token);
        String perfilClaim = decodedJWT.getClaim("perfil").asString();

        if (perfilClaim == null) {
            throw new UnauthorizedException();
        }

        String[] perfilData = perfilClaim.split("-");
        Long revendaId = Long.valueOf(perfilData[0]);
        CargosEnum cargoNome = CargosEnum.valueOf(perfilData[1]);

        return new CurrentPerfilDTO(revendaId, cargoNome);
    }
}
