package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Revenda.RevendaDTO;
import com.mobiauto.backend.application.dtos.Revenda.CreateRevendaDTO;
import com.mobiauto.backend.application.dtos.Revenda.UpdateRevendaDTO;
import com.mobiauto.backend.application.mappers.RevendaMapper;
import com.mobiauto.backend.application.utils.AuthorizationUtils;
import com.mobiauto.backend.domain.enums.CargosEnum;
import com.mobiauto.backend.domain.exceptions.Auth.UnauthorizedException;
import com.mobiauto.backend.domain.exceptions.Revenda.RevendaNotFoundException;
import com.mobiauto.backend.domain.models.Revenda;
import com.mobiauto.backend.domain.models.Usuario;
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
public class RevendaService {

    private final RevendaRepository revendaRepository;
    private final RevendaMapper revendaMapper;
    private final CodeGeneratorUtils codeGeneratorUtils;
    private final AuthorizationUtils authorizationUtils;

    @Autowired
    public RevendaService(RevendaRepository revendaRepository, RevendaMapper revendaMapper,
                          CodeGeneratorUtils codeGeneratorUtils, AuthorizationUtils authorizationUtils) {
        this.revendaRepository = revendaRepository;
        this.revendaMapper = revendaMapper;
        this.codeGeneratorUtils = codeGeneratorUtils;
        this.authorizationUtils = authorizationUtils;
    }

    public List<RevendaDTO> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authorizationUtils.isSuperuser(authentication)) {
            throw new UnauthorizedException();
        }
        return revendaRepository.findAll().stream()
                .map(revendaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RevendaDTO findById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Revenda revenda = revendaRepository.findById(id)
                .orElseThrow(RevendaNotFoundException::new);

        if (!authorizationUtils.isSuperuser(authentication) &&
                !authorizationUtils.hasAccessToRevenda(revenda.getId())) {
            throw new UnauthorizedException();
        }
        return revendaMapper.toDTO(revenda);
    }

    @Transactional
    public RevendaDTO createRevenda(CreateRevendaDTO createRevendaDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authorizationUtils.isSuperuser(authentication)) {
            throw new UnauthorizedException();
        }
        Revenda revenda = revendaMapper.toEntity(createRevendaDTO);
        revenda.setCodigo(codeGeneratorUtils.generateRevendaCodigo());
        revenda = revendaRepository.save(revenda);
        return revendaMapper.toDTO(revenda);
    }

    @Transactional
    public RevendaDTO updateRevenda(Long id, UpdateRevendaDTO updateRevendaDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Revenda existingRevenda = revendaRepository.findById(id)
                .orElseThrow(RevendaNotFoundException::new);

        boolean isSuperuser = authorizationUtils.isSuperuser(authentication);
        boolean isProprietarioDaRevenda = authorizationUtils.hasRole(CargosEnum.PROPRIETARIO) &&
                authorizationUtils.hasAccessToRevenda(existingRevenda.getId());

        if (!isSuperuser && !isProprietarioDaRevenda) {
            throw new UnauthorizedException();
        }

        revendaMapper.updateEntityFromDTO(updateRevendaDTO, existingRevenda);
        existingRevenda = revendaRepository.save(existingRevenda);
        return revendaMapper.toDTO(existingRevenda);
    }


    @Transactional
    public void deleteRevenda(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authorizationUtils.isSuperuser(authentication)) {
            throw new UnauthorizedException();
        }
        Revenda revenda = revendaRepository.findById(id)
                .orElseThrow(RevendaNotFoundException::new);
        revendaRepository.delete(revenda);
    }
}
