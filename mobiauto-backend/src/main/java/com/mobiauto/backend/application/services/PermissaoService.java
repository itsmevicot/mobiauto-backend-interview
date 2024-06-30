package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Permissao.PermissaoDTO;
import com.mobiauto.backend.application.dtos.Permissao.CreatePermissaoDTO;
import com.mobiauto.backend.application.dtos.Permissao.UpdatePermissaoDTO;
import com.mobiauto.backend.application.mappers.PermissaoMapper;
import com.mobiauto.backend.domain.exceptions.Permissao.PermissaoNotFoundException;
import com.mobiauto.backend.domain.models.Permissao;
import com.mobiauto.backend.domain.repositories.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;
    private final PermissaoMapper permissaoMapper;

    @Autowired
    public PermissaoService(PermissaoRepository permissaoRepository, PermissaoMapper permissaoMapper) {
        this.permissaoRepository = permissaoRepository;
        this.permissaoMapper = permissaoMapper;
    }

    public List<PermissaoDTO> findAll() {
        return permissaoRepository.findAll().stream()
                .map(permissaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PermissaoDTO findById(Long id) {
        Permissao permissao = permissaoRepository.findById(id)
                .orElseThrow(PermissaoNotFoundException::new);
        return permissaoMapper.toDTO(permissao);
    }

    @Transactional
    public PermissaoDTO createPermissao(CreatePermissaoDTO createPermissaoDTO) {
        Permissao permissao = permissaoMapper.toEntity(createPermissaoDTO);
        permissao = permissaoRepository.save(permissao);
        return permissaoMapper.toDTO(permissao);
    }

    @Transactional
    public PermissaoDTO updatePermissao(Long id, UpdatePermissaoDTO updatePermissaoDTO) {
        Permissao existingPermissao = permissaoRepository.findById(id)
                .orElseThrow(PermissaoNotFoundException::new);
        permissaoMapper.updateEntityFromDTO(updatePermissaoDTO, existingPermissao);
        existingPermissao = permissaoRepository.save(existingPermissao);
        return permissaoMapper.toDTO(existingPermissao);
    }

    @Transactional
    public void deletePermissao(Long id) {
        Permissao permissao = permissaoRepository.findById(id)
                .orElseThrow(PermissaoNotFoundException::new);
        permissaoRepository.delete(permissao);
    }
}
