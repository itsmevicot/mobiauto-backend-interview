package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Cliente.ClienteDTO;
import com.mobiauto.backend.application.dtos.Cliente.CreateClienteDTO;
import com.mobiauto.backend.application.dtos.Cliente.UpdateClienteDTO;
import com.mobiauto.backend.application.dtos.Perfil.CurrentPerfilDTO;
import com.mobiauto.backend.application.mappers.ClienteMapper;
import com.mobiauto.backend.application.utils.AuthorizationUtils;
import com.mobiauto.backend.domain.exceptions.Auth.UnauthorizedException;
import com.mobiauto.backend.domain.exceptions.Cliente.ClienteNotFoundException;
import com.mobiauto.backend.domain.exceptions.Cliente.EmailAlreadyExistsException;
import com.mobiauto.backend.domain.models.Cliente;
import com.mobiauto.backend.domain.repositories.ClienteRepository;
import com.mobiauto.backend.domain.repositories.OportunidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final AuthorizationUtils authorizationUtils;
    private final OportunidadeRepository oportunidadeRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper,
                          AuthorizationUtils authorizationUtils, OportunidadeRepository oportunidadeRepository) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.authorizationUtils = authorizationUtils;
        this.oportunidadeRepository = oportunidadeRepository;
    }

    public List<ClienteDTO> findAllActive() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authorizationUtils.isSuperuser(authentication)) {
            return clienteRepository.findAllByAtivoTrue().stream()
                    .map(clienteMapper::toDTO)
                    .collect(Collectors.toList());
        } else {
            CurrentPerfilDTO currentPerfil = authorizationUtils.getCurrentPerfil();
            return oportunidadeRepository.findClientesByRevendaIdAndAtivo(currentPerfil.revendaId(), true).stream()
                    .map(clienteMapper::toDTO)
                    .collect(Collectors.toList());
        }
    }

    public List<ClienteDTO> findAllInactive() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authorizationUtils.isSuperuser(authentication)) {
            return clienteRepository.findAllByAtivoFalse().stream()
                    .map(clienteMapper::toDTO)
                    .collect(Collectors.toList());
        }
        throw new UnauthorizedException();
    }

    public ClienteDTO findById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(ClienteNotFoundException::new);

        if (authorizationUtils.isSuperuser(authentication) ||
                hasAccessToRevendaByClienteId(cliente.getId())) {
            return clienteMapper.toDTO(cliente);
        }
        throw new UnauthorizedException();
    }

    @Transactional
    public ClienteDTO createCliente(CreateClienteDTO createClienteDTO) {
        if (authorizationUtils.getCurrentPerfil() == null) {
            throw new UnauthorizedException();
        }
        if (clienteRepository.existsByEmail(createClienteDTO.email())) {
            throw new EmailAlreadyExistsException();
        }
        Cliente cliente = clienteMapper.toEntity(createClienteDTO);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toDTO(cliente);
    }

    @Transactional
    public ClienteDTO updateCliente(Long id, UpdateClienteDTO updateClienteDTO) {
        if (authorizationUtils.getCurrentPerfil() == null) {
            throw new UnauthorizedException();
        }
        Cliente existingCliente = clienteRepository.findById(id)
                .orElseThrow(ClienteNotFoundException::new);

        Cliente clienteWithSameEmail = clienteRepository.findByEmail(updateClienteDTO.email());
        if (clienteWithSameEmail != null && !clienteWithSameEmail.getId().equals(id)) {
            throw new EmailAlreadyExistsException();
        }

        clienteMapper.updateEntityFromDTO(updateClienteDTO, existingCliente);
        existingCliente = clienteRepository.save(existingCliente);
        return clienteMapper.toDTO(existingCliente);
    }

    @Transactional
    public ClienteDTO reativarCliente(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authorizationUtils.isSuperuser(authentication)) {
            throw new UnauthorizedException();
        }
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(ClienteNotFoundException::new);
        cliente.setAtivo(true);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toDTO(cliente);
    }

    @Transactional
    public void delete(Long id, boolean hardDelete) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authorizationUtils.isSuperuser(authentication)) {
            throw new UnauthorizedException();
        }
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(ClienteNotFoundException::new);
        if (hardDelete) {
            clienteRepository.delete(cliente);
        } else {
            cliente.setAtivo(false);
            clienteRepository.save(cliente);
        }
    }

    private boolean hasAccessToRevendaByClienteId(Long clienteId) {
        CurrentPerfilDTO currentPerfil = authorizationUtils.getCurrentPerfil();
        return oportunidadeRepository.existsByClienteIdAndRevendaId(clienteId, currentPerfil.revendaId());
    }
}
