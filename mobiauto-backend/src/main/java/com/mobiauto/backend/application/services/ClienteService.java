package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Cliente.ClienteDTO;
import com.mobiauto.backend.application.dtos.Cliente.CreateClienteDTO;
import com.mobiauto.backend.application.dtos.Cliente.UpdateClienteDTO;
import com.mobiauto.backend.application.mappers.ClienteMapper;
import com.mobiauto.backend.domain.exceptions.Cliente.EmailAlreadyExistsException;
import com.mobiauto.backend.domain.exceptions.Cliente.ClienteNotFoundException;
import com.mobiauto.backend.domain.models.Cliente;
import com.mobiauto.backend.domain.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public List<ClienteDTO> findAllActive() {
        return clienteRepository.findAllByAtivoTrue().stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ClienteDTO> findAllInactive() {
        return clienteRepository.findAllByAtivoFalse().stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(ClienteNotFoundException::new);
        return clienteMapper.toDTO(cliente);
    }

    @Transactional
    public ClienteDTO createCliente(CreateClienteDTO createClienteDTO) {
        if (clienteRepository.existsByEmail(createClienteDTO.email())) {
            throw new EmailAlreadyExistsException();
        }
        Cliente cliente = clienteMapper.toEntity(createClienteDTO);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toDTO(cliente);
    }

    @Transactional
    public ClienteDTO updateCliente(Long id, UpdateClienteDTO updateClienteDTO) {
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
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(ClienteNotFoundException::new);
        cliente.setAtivo(true);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toDTO(cliente);
    }

    @Transactional
    public void delete(Long id, boolean hardDelete) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(ClienteNotFoundException::new);
        if (hardDelete) {
            clienteRepository.delete(cliente);
        } else {
            cliente.setAtivo(false);
            clienteRepository.save(cliente);
        }
    }
}
