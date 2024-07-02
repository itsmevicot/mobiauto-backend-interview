package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Perfil.PerfilDTO;
import com.mobiauto.backend.application.dtos.Perfil.CreatePerfilDTO;
import com.mobiauto.backend.application.dtos.Perfil.UpdatePerfilDTO;
import com.mobiauto.backend.application.mappers.PerfilMapper;
import com.mobiauto.backend.application.utils.AuthorizationUtils;
import com.mobiauto.backend.domain.enums.CargosEnum;
import com.mobiauto.backend.domain.exceptions.Auth.UnauthorizedException;
import com.mobiauto.backend.domain.exceptions.Perfil.PerfilNotFoundException;
import com.mobiauto.backend.domain.exceptions.Revenda.RevendaNotFoundException;
import com.mobiauto.backend.domain.exceptions.Usuario.UsuarioNotFoundException;
import com.mobiauto.backend.domain.models.Perfil;
import com.mobiauto.backend.domain.models.Revenda;
import com.mobiauto.backend.domain.models.Usuario;
import com.mobiauto.backend.domain.repositories.PerfilRepository;
import com.mobiauto.backend.domain.repositories.RevendaRepository;
import com.mobiauto.backend.domain.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final RevendaRepository revendaRepository;
    private final PerfilMapper perfilMapper;
    private final AuthorizationUtils authorizationUtils;

    @Autowired
    public PerfilService(PerfilRepository perfilRepository, PerfilMapper perfilMapper, UsuarioRepository usuarioRepository,
                         RevendaRepository revendaRepository, AuthorizationUtils authorizationUtils) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
        this.revendaRepository = revendaRepository;
        this.perfilMapper = perfilMapper;
        this.authorizationUtils = authorizationUtils;
    }

    public List<PerfilDTO> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authorizationUtils.isSuperuser(authentication)) {
            return perfilRepository.findAll().stream()
                    .map(perfilMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (authorizationUtils.hasRole(CargosEnum.PROPRIETARIO) || authorizationUtils.hasRole(CargosEnum.GERENTE)) {
            Long revendaId = authorizationUtils.getCurrentPerfil().revendaId();
            return perfilRepository.findByRevendaId(revendaId).stream()
                    .map(perfilMapper::toDTO)
                    .collect(Collectors.toList());
        }
        throw new UnauthorizedException();
    }

    public PerfilDTO findById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(PerfilNotFoundException::new);

        if (authorizationUtils.isSuperuser(authentication) || authorizationUtils.hasAccessToRevenda(perfil.getRevenda().getId())) {
            return perfilMapper.toDTO(perfil);
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public PerfilDTO createPerfil(CreatePerfilDTO createPerfilDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        CargosEnum newCargo = createPerfilDTO.cargo();

        if (!authorizationUtils.isAuthorizedToCreatePerfil(createPerfilDTO.revendaId(), newCargo) || !authorizationUtils.canAssignRole(usuario, newCargo)) {
            throw new UnauthorizedException();
        }

        Usuario newUsuario = usuarioRepository.findById(createPerfilDTO.usuarioId())
                .orElseThrow(UsuarioNotFoundException::new);
        Revenda revenda = revendaRepository.findById(createPerfilDTO.revendaId())
                .orElseThrow(RevendaNotFoundException::new);

        Perfil perfil = perfilMapper.toEntity(createPerfilDTO, newUsuario, revenda);
        perfil = perfilRepository.save(perfil);
        return perfilMapper.toDTO(perfil);
    }

    @Transactional
    public PerfilDTO updatePerfil(Long id, UpdatePerfilDTO updatePerfilDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        CargosEnum newCargo = updatePerfilDTO.cargo();

        Perfil existingPerfil = perfilRepository.findById(id)
                .orElseThrow(PerfilNotFoundException::new);

        if (!authorizationUtils.isSuperuser(authentication) &&
                !(authorizationUtils.hasRole(CargosEnum.PROPRIETARIO) && authorizationUtils.hasAccessToRevenda(existingPerfil.getRevenda().getId()))) {
            throw new UnauthorizedException();
        }

        if (!authorizationUtils.canAssignRole(usuario, newCargo)) {
            throw new UnauthorizedException();
        }

        perfilMapper.updateEntityFromDTO(updatePerfilDTO, existingPerfil);
        existingPerfil = perfilRepository.save(existingPerfil);
        return perfilMapper.toDTO(existingPerfil);
    }

    @Transactional
    public void deletePerfil(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(PerfilNotFoundException::new);

        if (!authorizationUtils.isSuperuser(authentication) &&
                !authorizationUtils.hasRole(CargosEnum.PROPRIETARIO)) {
            throw new UnauthorizedException();
        }

        perfilRepository.delete(perfil);
    }

    public List<PerfilDTO> findByRevenda(Long revendaId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authorizationUtils.isSuperuser(authentication) && !authorizationUtils.hasAccessToRevenda(revendaId)) {
            throw new UnauthorizedException();
        }

        List<Perfil> perfis = perfilRepository.findByRevendaId(revendaId);
        return perfis.stream().map(perfilMapper::toDTO).collect(Collectors.toList());
    }
}
