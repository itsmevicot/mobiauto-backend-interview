package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Revenda.RevendaDTO;
import com.mobiauto.backend.application.dtos.Revenda.CreateRevendaDTO;
import com.mobiauto.backend.application.dtos.Revenda.UpdateRevendaDTO;
import com.mobiauto.backend.application.mappers.RevendaMapper;
import com.mobiauto.backend.domain.exceptions.Revenda.RevendaNotFoundException;
import com.mobiauto.backend.domain.models.Revenda;
import com.mobiauto.backend.domain.repositories.RevendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RevendaService {

    private final RevendaRepository revendaRepository;
    private final RevendaMapper revendaMapper;

    @Autowired
    public RevendaService(RevendaRepository revendaRepository, RevendaMapper revendaMapper) {
        this.revendaRepository = revendaRepository;
        this.revendaMapper = revendaMapper;
    }

    public List<RevendaDTO> findAll() {
        return revendaRepository.findAll().stream()
                .map(revendaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RevendaDTO findById(Long id) {
        Revenda revenda = revendaRepository.findById(id)
                .orElseThrow(RevendaNotFoundException::new);
        return revendaMapper.toDTO(revenda);
    }

    @Transactional
    public RevendaDTO createRevenda(CreateRevendaDTO createRevendaDTO) {
        Revenda revenda = revendaMapper.toEntity(createRevendaDTO);
        revenda = revendaRepository.save(revenda);
        return revendaMapper.toDTO(revenda);
    }

    @Transactional
    public RevendaDTO updateRevenda(Long id, UpdateRevendaDTO updateRevendaDTO) {
        Revenda existingRevenda = revendaRepository.findById(id)
                .orElseThrow(RevendaNotFoundException::new);
        revendaMapper.updateEntityFromDTO(updateRevendaDTO, existingRevenda);
        existingRevenda = revendaRepository.save(existingRevenda);
        return revendaMapper.toDTO(existingRevenda);
    }

    @Transactional
    public void deleteRevenda(Long id) {
        Revenda revenda = revendaRepository.findById(id)
                .orElseThrow(RevendaNotFoundException::new);
        revendaRepository.delete(revenda);
    }
}
