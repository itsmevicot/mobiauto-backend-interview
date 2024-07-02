package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Perfil.PerfilDTO;
import com.mobiauto.backend.application.dtos.Perfil.CreatePerfilDTO;
import com.mobiauto.backend.application.dtos.Perfil.UpdatePerfilDTO;
import com.mobiauto.backend.application.mappers.PerfilMapper;
import com.mobiauto.backend.domain.enums.CargosEnum;
import com.mobiauto.backend.domain.exceptions.Auth.UnauthorizedException;
import com.mobiauto.backend.domain.exceptions.Cargo.CargoNotFoundException;
import com.mobiauto.backend.domain.exceptions.Perfil.PerfilNotFoundException;
import com.mobiauto.backend.domain.exceptions.Revenda.RevendaNotFoundException;
import com.mobiauto.backend.domain.exceptions.Usuario.UsuarioNotFoundException;
import com.mobiauto.backend.domain.models.Perfil;
import com.mobiauto.backend.domain.models.Revenda;
import com.mobiauto.backend.domain.models.Usuario;
import com.mobiauto.backend.domain.models.Cargo;
import com.mobiauto.backend.domain.repositories.PerfilRepository;
import com.mobiauto.backend.domain.repositories.RevendaRepository;
import com.mobiauto.backend.domain.repositories.UsuarioRepository;
import com.mobiauto.backend.domain.repositories.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final RevendaRepository revendaRepository;
    private final CargoRepository cargoRepository;
    private final PerfilMapper perfilMapper;

    @Autowired
    public PerfilService(PerfilRepository perfilRepository, PerfilMapper perfilMapper, UsuarioRepository usuarioRepository,
                         RevendaRepository revendaRepository, CargoRepository cargoRepository) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
        this.revendaRepository = revendaRepository;
        this.cargoRepository = cargoRepository;
        this.perfilMapper = perfilMapper;
    }

    public List<PerfilDTO> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        if (isAdmin) {
            return perfilRepository.findAll().stream()
                    .map(perfilMapper::toDTO)
                    .collect(Collectors.toList());
        }

        Set<Perfil> userPerfis = usuario.getPerfis();
        List<Long> userRevendaIds = userPerfis.stream()
                .map(perfil -> perfil.getRevenda().getId())
                .collect(Collectors.toList());

        boolean isProprietario = userPerfis.stream()
                .anyMatch(perfil -> perfil.getCargo().getNome() == CargosEnum.PROPRIETARIO);

        boolean isGerente = userPerfis.stream()
                .anyMatch(perfil -> perfil.getCargo().getNome() == CargosEnum.GERENTE);

        if (isProprietario) {
            return perfilRepository.findAll().stream()
                    .filter(perfil -> userRevendaIds.contains(perfil.getRevenda().getId()))
                    .map(perfilMapper::toDTO)
                    .collect(Collectors.toList());
        }

        if (isGerente) {
            return perfilRepository.findAll().stream()
                    .filter(perfil -> userRevendaIds.contains(perfil.getRevenda().getId()) &&
                            (perfil.getCargo().getNome() == CargosEnum.GERENTE || perfil.getCargo().getNome() == CargosEnum.ASSISTENTE))
                    .map(perfilMapper::toDTO)
                    .collect(Collectors.toList());
        }

        throw new UnauthorizedException();
    }

    public PerfilDTO findById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(PerfilNotFoundException::new);

        if (isAdmin) {
            return perfilMapper.toDTO(perfil);
        }

        Set<Perfil> userPerfis = usuario.getPerfis();
        List<Long> userRevendaIds = userPerfis.stream()
                .map(userPerfil -> userPerfil.getRevenda().getId())
                .collect(Collectors.toList());

        boolean isProprietario = userPerfis.stream()
                .anyMatch(userPerfil -> userPerfil.getCargo().getNome() == CargosEnum.PROPRIETARIO);

        boolean isGerente = userPerfis.stream()
                .anyMatch(userPerfil -> userPerfil.getCargo().getNome() == CargosEnum.GERENTE);

        if (isProprietario && userRevendaIds.contains(perfil.getRevenda().getId())) {
            return perfilMapper.toDTO(perfil);
        }

        if (isGerente && userRevendaIds.contains(perfil.getRevenda().getId()) &&
                (perfil.getCargo().getNome() == CargosEnum.GERENTE || perfil.getCargo().getNome() == CargosEnum.ASSISTENTE)) {
            return perfilMapper.toDTO(perfil);
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public PerfilDTO createPerfil(CreatePerfilDTO createPerfilDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        CargosEnum newCargo = CargosEnum.valueOf(createPerfilDTO.cargoId().toString());

        boolean authorized = isSuperuser(authentication) || usuario.getPerfis().stream()
                .anyMatch(perfil -> perfil.getCargo().getNome() == CargosEnum.PROPRIETARIO ||
                        (perfil.getCargo().getNome() == CargosEnum.GERENTE && newCargo == CargosEnum.ASSISTENTE));

        if (!authorized) {
            throw new UnauthorizedException();
        }

        if (!canAssignRole(usuario, newCargo)) {
            throw new UnauthorizedException();
        }

        Usuario newUsuario = usuarioRepository.findById(createPerfilDTO.usuarioId())
                .orElseThrow(UsuarioNotFoundException::new);
        Revenda revenda = revendaRepository.findById(createPerfilDTO.revendaId())
                .orElseThrow(RevendaNotFoundException::new);
        Cargo cargo = cargoRepository.findById(createPerfilDTO.cargoId())
                .orElseThrow(CargoNotFoundException::new);

        Perfil perfil = perfilMapper.toEntity(createPerfilDTO, newUsuario, revenda, cargo);
        perfil = perfilRepository.save(perfil);
        return perfilMapper.toDTO(perfil);
    }

    @Transactional
    public PerfilDTO updatePerfil(Long id, UpdatePerfilDTO updatePerfilDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        CargosEnum newCargo = CargosEnum.valueOf(updatePerfilDTO.cargoId().toString());

        boolean authorized = isSuperuser(authentication) || usuario.getPerfis().stream()
                .anyMatch(perfil -> perfil.getCargo().getNome() == CargosEnum.PROPRIETARIO);

        if (!authorized) {
            throw new UnauthorizedException();
        }

        if (!canAssignRole(usuario, newCargo)) {
            throw new UnauthorizedException();
        }

        Perfil existingPerfil = perfilRepository.findById(id)
                .orElseThrow(PerfilNotFoundException::new);

        Cargo cargo = cargoRepository.findById(updatePerfilDTO.cargoId())
                .orElseThrow(CargoNotFoundException::new);

        perfilMapper.updateEntityFromDTO(updatePerfilDTO, existingPerfil, cargo);
        existingPerfil = perfilRepository.save(existingPerfil);
        return perfilMapper.toDTO(existingPerfil);
    }

    @Transactional
    public void deletePerfil(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        boolean authorized = isSuperuser(authentication) || usuario.getPerfis().stream()
                .anyMatch(perfil -> perfil.getCargo().getNome() == CargosEnum.PROPRIETARIO);

        if (!authorized) {
            throw new UnauthorizedException();
        }

        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(PerfilNotFoundException::new);
        perfilRepository.delete(perfil);
    }

    public List<PerfilDTO> findByRevenda(Long revendaId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        boolean authorized = isSuperuser(authentication) || usuario.getPerfis().stream()
                .anyMatch(perfil -> perfil.getRevenda().getId().equals(revendaId));

        if (!authorized) {
            throw new UnauthorizedException();
        }

        List<Perfil> perfis = perfilRepository.findByRevendaId(revendaId);
        return perfis.stream().map(perfilMapper::toDTO).collect(Collectors.toList());
    }

    private boolean isSuperuser(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    }

    private boolean canAssignRole(Usuario usuario, CargosEnum newCargo) {
        List<CargosEnum> assignableRoles = Arrays.asList(CargosEnum.values());

        if (isSuperuser(SecurityContextHolder.getContext().getAuthentication())) {
            return true;
        }

        CargosEnum highestRole = usuario.getPerfis().stream()
                .map(perfil -> perfil.getCargo().getNome())
                .max(Enum::compareTo)
                .orElse(CargosEnum.ASSISTENTE);

        return assignableRoles.indexOf(newCargo) <= assignableRoles.indexOf(highestRole);
    }
}
