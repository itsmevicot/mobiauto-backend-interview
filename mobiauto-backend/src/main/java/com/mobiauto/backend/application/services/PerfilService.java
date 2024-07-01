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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final PerfilMapper perfilMapper;
    private final UsuarioRepository usuarioRepository;
    private final RevendaRepository revendaRepository;
    private final CargoRepository cargoRepository;

    @Autowired
    public PerfilService(PerfilRepository perfilRepository, PerfilMapper perfilMapper, UsuarioRepository usuarioRepository, RevendaRepository revendaRepository, CargoRepository cargoRepository) {
        this.perfilRepository = perfilRepository;
        this.perfilMapper = perfilMapper;
        this.usuarioRepository = usuarioRepository;
        this.revendaRepository = revendaRepository;
        this.cargoRepository = cargoRepository;
    }

    public List<PerfilDTO> findAll() {
        return perfilRepository.findAll().stream()
                .map(perfilMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PerfilDTO findById(Long id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(PerfilNotFoundException::new);
        return perfilMapper.toDTO(perfil);
    }

    @Transactional
    public PerfilDTO createPerfil(CreatePerfilDTO createPerfilDTO, Long usuarioIdCriador) {
        Usuario usuario = usuarioRepository.findById(createPerfilDTO.usuarioId())
                .orElseThrow(UsuarioNotFoundException::new);
        Revenda revenda = revendaRepository.findById(createPerfilDTO.revendaId())
                .orElseThrow(RevendaNotFoundException::new);
        Cargo cargo = cargoRepository.findById(createPerfilDTO.cargoId())
                .orElseThrow(CargoNotFoundException::new);

        Usuario usuarioCriador = usuarioRepository.findById(usuarioIdCriador)
                .orElseThrow(UsuarioNotFoundException::new);

        if (!isAuthorizedToCreatePerfil(usuarioCriador, revenda, cargo)) {
            throw new UnauthorizedException();
        }

        Perfil perfil = perfilMapper.toEntity(createPerfilDTO, usuario, revenda, cargo);
        perfil = perfilRepository.save(perfil);
        return perfilMapper.toDTO(perfil);
    }

    private boolean isAuthorizedToCreatePerfil(Usuario usuarioCriador, Revenda revenda, Cargo cargo) {
        for (Perfil perfil : usuarioCriador.getPerfis()) {
            if (perfil.getRevenda().equals(revenda)) {
                CargosEnum cargoNome = perfil.getCargo().getNome();
                System.out.println("Checking cargo: " + cargoNome); // Debug statement

                if (cargoNome == CargosEnum.PROPRIETARIO ||
                        (cargoNome == CargosEnum.GERENTE && cargo.getNome() == CargosEnum.ASSISTENTE)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Transactional
    public PerfilDTO updatePerfil(Long id, UpdatePerfilDTO updatePerfilDTO) {
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
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(PerfilNotFoundException::new);
        perfilRepository.delete(perfil);
    }
}

