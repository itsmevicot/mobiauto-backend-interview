package com.mobiauto.backend.application.services;
import com.mobiauto.backend.domain.exceptions.Cliente.EmailAlreadyExistsException;
import com.mobiauto.backend.domain.models.Cliente;
import com.mobiauto.backend.domain.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> findAllActive() {
        return clienteRepository.findAllByAtivoTrue();
    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente not found"));
    }

    public Cliente createCliente(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new EmailAlreadyExistsException("E-mail já cadastrado: " + cliente.getEmail());
        }
        return clienteRepository.save(cliente);
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void delete(Long id, boolean hardDelete) {
        Cliente cliente = findById(id);
        if (hardDelete) {
            clienteRepository.delete(cliente);
        } else {
            cliente.setAtivo(false);
            clienteRepository.save(cliente);
        }
    }
}