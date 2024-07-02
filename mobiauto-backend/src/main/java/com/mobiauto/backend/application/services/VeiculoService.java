package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Veiculo.VeiculoDTO;
import com.mobiauto.backend.application.dtos.Veiculo.CreateVeiculoDTO;
import com.mobiauto.backend.application.dtos.Veiculo.UpdateVeiculoDTO;
import com.mobiauto.backend.application.mappers.VeiculoMapper;
import com.mobiauto.backend.application.utils.AuthorizationUtils;
import com.mobiauto.backend.domain.enums.CargosEnum;
import com.mobiauto.backend.domain.exceptions.Auth.UnauthorizedException;
import com.mobiauto.backend.domain.exceptions.Revenda.RevendaNotFoundException;
import com.mobiauto.backend.domain.exceptions.Veiculo.VeiculoNotFoundException;
import com.mobiauto.backend.domain.models.Veiculo;
import com.mobiauto.backend.domain.models.Revenda;
import com.mobiauto.backend.domain.repositories.VeiculoRepository;
import com.mobiauto.backend.domain.repositories.RevendaRepository;
import com.mobiauto.backend.application.utils.CodeGeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthorizationUtils authorizationUtils;

    @Autowired
    public VeiculoService(VeiculoRepository veiculoRepository, VeiculoMapper veiculoMapper,
                          RevendaRepository revendaRepository, CodeGeneratorUtils codeGeneratorUtil,
                          AuthorizationUtils authorizationUtils) {
        this.veiculoRepository = veiculoRepository;
        this.veiculoMapper = veiculoMapper;
        this.revendaRepository = revendaRepository;
        this.codeGeneratorUtil = codeGeneratorUtil;
        this.authorizationUtils = authorizationUtils;
    }

    public List<VeiculoDTO> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authorizationUtils.isSuperuser(authentication)) {
            return veiculoRepository.findAll().stream()
                    .map(veiculoMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (authorizationUtils.hasRole(CargosEnum.PROPRIETARIO) ||
                authorizationUtils.hasRole(CargosEnum.GERENTE) ||
                authorizationUtils.hasRole(CargosEnum.ASSISTENTE)) {
            Long revendaId = authorizationUtils.getCurrentPerfil().revendaId();
            return veiculoRepository.findByRevendaId(revendaId).stream()
                    .map(veiculoMapper::toDTO)
                    .collect(Collectors.toList());
        }
        throw new UnauthorizedException();
    }

    public VeiculoDTO findById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(VeiculoNotFoundException::new);

        if (authorizationUtils.isSuperuser(authentication) ||
                authorizationUtils.hasAccessToRevenda(veiculo.getRevenda().getId())) {
            return veiculoMapper.toDTO(veiculo);
        }
        throw new UnauthorizedException();
    }

    public List<VeiculoDTO> findByRevenda(Long revendaId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authorizationUtils.isSuperuser(authentication) ||
                authorizationUtils.hasAccessToRevenda(revendaId)) {
            return veiculoRepository.findByRevendaId(revendaId).stream()
                    .map(veiculoMapper::toDTO)
                    .collect(Collectors.toList());
        }
        throw new UnauthorizedException();
    }

    @Transactional
    public VeiculoDTO createVeiculo(CreateVeiculoDTO createVeiculoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authorizationUtils.isSuperuser(authentication) && !authorizationUtils.hasRole(CargosEnum.PROPRIETARIO)) {
            throw new UnauthorizedException();
        }

        Revenda revenda = revendaRepository.findById(createVeiculoDTO.revendaId())
                .orElseThrow(RevendaNotFoundException::new);

        Veiculo veiculo = veiculoMapper.toEntity(createVeiculoDTO, revenda);
        veiculo.setCodigo(codeGeneratorUtil.generateVeiculoCodigo());
        veiculo = veiculoRepository.save(veiculo);
        return veiculoMapper.toDTO(veiculo);
    }

    @Transactional
    public VeiculoDTO updateVeiculo(Long id, UpdateVeiculoDTO updateVeiculoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authorizationUtils.isSuperuser(authentication) && !authorizationUtils.hasRole(CargosEnum.PROPRIETARIO)) {
            throw new UnauthorizedException();
        }

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authorizationUtils.isSuperuser(authentication) && !authorizationUtils.hasRole(CargosEnum.PROPRIETARIO)) {
            throw new UnauthorizedException();
        }
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(VeiculoNotFoundException::new);
        veiculoRepository.delete(veiculo);
    }
}
