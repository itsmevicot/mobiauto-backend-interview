package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Oportunidade.OportunidadeDTO;
import com.mobiauto.backend.application.dtos.Perfil.CurrentPerfilDTO;
import com.mobiauto.backend.application.dtos.Usuario.UsuarioDTO;
import com.mobiauto.backend.application.dtos.Usuario.CreateUsuarioDTO;
import com.mobiauto.backend.application.dtos.Usuario.UpdateUsuarioDTO;
import com.mobiauto.backend.application.mappers.OportunidadeMapper;
import com.mobiauto.backend.application.mappers.UsuarioMapper;
import com.mobiauto.backend.application.utils.AuthorizationUtils;
import com.mobiauto.backend.domain.enums.CargosEnum;
import com.mobiauto.backend.domain.exceptions.Auth.UnauthorizedException;
import com.mobiauto.backend.domain.exceptions.Usuario.EmailAlreadyExistsException;
import com.mobiauto.backend.domain.exceptions.Usuario.UsuarioNotFoundException;
import com.mobiauto.backend.application.utils.CodeGeneratorUtils;
import com.mobiauto.backend.domain.models.Usuario;
import com.mobiauto.backend.domain.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final OportunidadeMapper oportunidadeMapper;
    private final PasswordEncoder passwordEncoder;
    private final CodeGeneratorUtils codeGeneratorUtils;
    private final AuthorizationUtils authorizationUtils;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper,
                          OportunidadeMapper oportunidadeMapper, PasswordEncoder passwordEncoder,
                          CodeGeneratorUtils codeGeneratorUtils, AuthorizationUtils authorizationUtils) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.oportunidadeMapper = oportunidadeMapper;
        this.passwordEncoder = passwordEncoder;
        this.codeGeneratorUtils = codeGeneratorUtils;
        this.authorizationUtils = authorizationUtils;
    }

    public List<UsuarioDTO> findAllActive() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authorizationUtils.isSuperuser(authentication)) {
            return usuarioRepository.findAllByAtivoTrue().stream()
                    .map(usuarioMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (authorizationUtils.hasRole(CargosEnum.PROPRIETARIO) || authorizationUtils.hasRole(CargosEnum.GERENTE)) {
            CurrentPerfilDTO currentPerfil = authorizationUtils.getCurrentPerfil();
            return usuarioRepository.findAllByAtivoTrueAndRevendaId(currentPerfil.revendaId()).stream()
                    .map(usuarioMapper::toDTO)
                    .collect(Collectors.toList());
        }
        throw new UnauthorizedException();
    }

    public List<UsuarioDTO> findAllInactive() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authorizationUtils.isSuperuser(authentication)) {
            return usuarioRepository.findAllByAtivoFalse().stream()
                    .map(usuarioMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (authorizationUtils.hasRole(CargosEnum.PROPRIETARIO) || authorizationUtils.hasRole(CargosEnum.GERENTE)) {
            CurrentPerfilDTO currentPerfil = authorizationUtils.getCurrentPerfil();
            return usuarioRepository.findAllByAtivoFalseAndRevendaId(currentPerfil.revendaId()).stream()
                    .map(usuarioMapper::toDTO)
                    .collect(Collectors.toList());
        }
        throw new UnauthorizedException();
    }

    public UsuarioDTO findById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        if (authorizationUtils.isSuperuser(authentication) ||
                authorizationUtils.hasAccessToRevenda(usuario.getPerfis().stream()
                        .findFirst()
                        .orElseThrow(UnauthorizedException::new).getRevenda().getId())) {
            return usuarioMapper.toDTO(usuario);
        }
        throw new UnauthorizedException();
    }

    @Transactional
    public UsuarioDTO createUsuario(CreateUsuarioDTO createUsuarioDTO) {
        if (usuarioRepository.existsByEmail(createUsuarioDTO.email())) {
            throw new EmailAlreadyExistsException();
        }
        Usuario usuario = usuarioMapper.toEntity(createUsuarioDTO);
        usuario.setSenha(passwordEncoder.encode(createUsuarioDTO.senha()));
        usuario.setCodigo(codeGeneratorUtils.generateUsuarioCodigo());
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(usuario);
    }

    @Transactional
    public UsuarioDTO updateUsuario(Long id, UpdateUsuarioDTO updateUsuarioDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNotFoundException::new);

        if (!authorizationUtils.isSuperuser(authentication) &&
                !existingUsuario.getId().equals(((Usuario) authentication.getPrincipal()).getId())) {
            throw new UnauthorizedException();
        }

        Optional<Usuario> optionalUsuarioWithSameEmail = usuarioRepository.findByEmail(updateUsuarioDTO.email());
        if (optionalUsuarioWithSameEmail.isPresent() && !optionalUsuarioWithSameEmail.get().getId().equals(id)) {
            throw new EmailAlreadyExistsException();
        }

        usuarioMapper.updateEntityFromDTO(updateUsuarioDTO, existingUsuario);
        existingUsuario.setSenha(passwordEncoder.encode(updateUsuarioDTO.senha()));
        existingUsuario = usuarioRepository.save(existingUsuario);
        return usuarioMapper.toDTO(existingUsuario);
    }

    @Transactional
    public UsuarioDTO reativarUsuario(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authorizationUtils.isSuperuser(authentication)) {
            throw new UnauthorizedException();
        }
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        usuario.setAtivo(true);
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(usuario);
    }

    @Transactional
    public void delete(Long id, boolean hardDelete) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authorizationUtils.isSuperuser(authentication)) {
            throw new UnauthorizedException();
        }
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        if (hardDelete) {
            usuarioRepository.delete(usuario);
        } else {
            usuario.setAtivo(false);
            usuarioRepository.save(usuario);
        }
    }

    public List<OportunidadeDTO> findOportunidadesByUsuario(Long usuarioId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(UsuarioNotFoundException::new);

        if (authorizationUtils.isSuperuser(authentication)) {
            return usuario.getOportunidades().stream()
                    .map(oportunidadeMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (authorizationUtils.hasRole(CargosEnum.PROPRIETARIO) || authorizationUtils.hasRole(CargosEnum.GERENTE)) {
            if (!authorizationUtils.hasAccessToRevenda(usuario.getPerfis().stream()
                    .findFirst()
                    .orElseThrow(UnauthorizedException::new).getRevenda().getId())) {
                throw new UnauthorizedException();
            }
            return usuario.getOportunidades().stream()
                    .map(oportunidadeMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (authorizationUtils.hasRole(CargosEnum.ASSISTENTE)) {
            if (!usuarioId.equals(((Usuario) authentication.getPrincipal()).getId())) {
                throw new UnauthorizedException();
            }
            return usuario.getOportunidades().stream()
                    .map(oportunidadeMapper::toDTO)
                    .collect(Collectors.toList());
        }
        throw new UnauthorizedException();
    }
}

