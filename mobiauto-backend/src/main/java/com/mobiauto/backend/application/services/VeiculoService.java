package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Veiculo.VeiculoDTO;
import com.mobiauto.backend.application.dtos.Veiculo.CreateVeiculoDTO;
import com.mobiauto.backend.application.dtos.Veiculo.UpdateVeiculoDTO;
import com.mobiauto.backend.application.mappers.VeiculoMapper;
import com.mobiauto.backend.domain.exceptions.Revenda.RevendaNotFoundException;
import com.mobiauto.backend.domain.exceptions.Veiculo.VeiculoNotFoundException;
import com.mobiauto.backend.domain.models.Veiculo;
import com.mobiauto.backend.domain.models.Revenda;
import com.mobiauto.backend.domain.repositories.VeiculoRepository;
import com.mobiauto.backend.domain.repositories.RevendaRepository;
import com.mobiauto.backend.application.utils.CodeGeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final RevendaRepository revendaRepository;
    private final VeiculoMapper veiculoMapper;
    private final CodeGeneratorUtils codeGeneratorUtil;


    @Autowired
    public VeiculoService(VeiculoRepository veiculoRepository, VeiculoMapper veiculoMapper,
                          RevendaRepository revendaRepository, CodeGeneratorUtils codeGeneratorUtil) {
        this.veiculoRepository = veiculoRepository;
        this.veiculoMapper = veiculoMapper;
        this.revendaRepository = revendaRepository;
        this.codeGeneratorUtil = codeGeneratorUtil;
    }

    public List<VeiculoDTO> findAll() {
        return veiculoRepository.findAll().stream()
                .map(veiculoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VeiculoDTO findById(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(VeiculoNotFoundException::new);
        return veiculoMapper.toDTO(veiculo);
    }

    public List<VeiculoDTO> findByRevenda(Long revendaId) {
        return veiculoRepository.findByRevendaId(revendaId).stream()
                .map(veiculoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VeiculoDTO createVeiculo(CreateVeiculoDTO createVeiculoDTO) {
        Revenda revenda = revendaRepository.findById(createVeiculoDTO.revendaId())
                .orElseThrow(RevendaNotFoundException::new);

        Veiculo veiculo = veiculoMapper.toEntity(createVeiculoDTO, revenda);
        veiculo.setCodigo(codeGeneratorUtil.generateVeiculoCodigo());
        veiculo = veiculoRepository.save(veiculo);
        return veiculoMapper.toDTO(veiculo);
    }

    @Transactional
    public VeiculoDTO updateVeiculo(Long id, UpdateVeiculoDTO updateVeiculoDTO) {
        Veiculo existingVeiculo = veiculoRepository.findById(id)
                .orElseThrow(VeiculoNotFoundException::new);

        Revenda revenda = revendaRepository.findById(updateVeiculoDTO.revendaId())
                .orElseThrow(RevendaNotFoundException::new);

        veiculoMapper.updateEntityFromDTO(updateVeiculoDTO, existingVeiculo, revenda);
        existingVeiculo = veiculoRepository.save(existingVeiculo);
        return veiculoMapper.toDTO(existingVeiculo);
    }

    @Transactional
    public void deleteVeiculo(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(VeiculoNotFoundException::new);
        veiculoRepository.delete(veiculo);
    }
}
