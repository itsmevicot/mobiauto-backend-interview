package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Oportunidade.OportunidadeDTO;
import com.mobiauto.backend.application.dtos.Usuario.UsuarioDTO;
import com.mobiauto.backend.application.dtos.Usuario.CreateUsuarioDTO;
import com.mobiauto.backend.application.dtos.Usuario.UpdateUsuarioDTO;
import com.mobiauto.backend.application.mappers.OportunidadeMapper;
import com.mobiauto.backend.application.mappers.UsuarioMapper;
import com.mobiauto.backend.application.mappers.VeiculoMapper;
import com.mobiauto.backend.domain.exceptions.Usuario.EmailAlreadyExistsException;
import com.mobiauto.backend.domain.exceptions.Usuario.UsuarioNotFoundException;
import com.mobiauto.backend.domain.models.Usuario;
import com.mobiauto.backend.domain.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final OportunidadeMapper oportunidadeMapper;
    private final VeiculoMapper veiculoMapper;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, OportunidadeMapper oportunidadeMapper, VeiculoMapper veiculoMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.oportunidadeMapper = oportunidadeMapper;
        this.veiculoMapper = veiculoMapper;
    }

    public List<UsuarioDTO> findAllActive() {
        return usuarioRepository.findAllByAtivoTrue().stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioDTO> findAllInactive() {
        return usuarioRepository.findAllByAtivoFalse().stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNotFoundException::new);
        return usuarioMapper.toDTO(usuario);
    }

    @Transactional
    public UsuarioDTO createUsuario(CreateUsuarioDTO createUsuarioDTO) {
        if (usuarioRepository.existsByEmail(createUsuarioDTO.email())) {
            throw new EmailAlreadyExistsException();
        }
        Usuario usuario = usuarioMapper.toEntity(createUsuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(usuario);
    }

    @Transactional
    public UsuarioDTO updateUsuario(Long id, UpdateUsuarioDTO updateUsuarioDTO) {
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNotFoundException::new);

        Usuario usuarioWithSameEmail = usuarioRepository.findByEmail(updateUsuarioDTO.email());
        if (usuarioWithSameEmail != null && !usuarioWithSameEmail.getId().equals(id)) {
            throw new EmailAlreadyExistsException();
        }

        usuarioMapper.updateEntityFromDTO(updateUsuarioDTO, existingUsuario);
        existingUsuario = usuarioRepository.save(existingUsuario);
        return usuarioMapper.toDTO(existingUsuario);
    }

    @Transactional
    public UsuarioDTO reativarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNotFoundException::new);
        usuario.setAtivo(true);
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(usuario);
    }

    @Transactional
    public void delete(Long id, boolean hardDelete) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNotFoundException::new);
        if (hardDelete) {
            usuarioRepository.delete(usuario);
        } else {
            usuario.setAtivo(false);
            usuarioRepository.save(usuario);
        }
    }

    public List<OportunidadeDTO> findOportunidadesByUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(UsuarioNotFoundException::new);
        return usuario.getOportunidades().stream()
                .map(oportunidadeMapper::toDTO)
                .collect(Collectors.toList());
    }
}
