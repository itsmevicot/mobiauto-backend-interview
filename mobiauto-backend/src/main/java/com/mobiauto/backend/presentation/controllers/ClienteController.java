package com.mobiauto.backend.presentation.controllers;


import com.mobiauto.backend.application.dtos.Cliente.ClienteDTO;
import com.mobiauto.backend.application.dtos.Cliente.CreateClienteDTO;
import com.mobiauto.backend.application.dtos.Cliente.UpdateClienteDTO;
import com.mobiauto.backend.application.mappers.ClienteMapper;
import com.mobiauto.backend.application.services.ClienteService;
import com.mobiauto.backend.domain.models.Cliente;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    @Autowired
    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @GetMapping
    public List<ClienteDTO> getAllActiveClientes() {
        return clienteService.findAllActive().stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.ok(clienteMapper.toDTO(cliente));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> createCliente(@Valid @RequestBody CreateClienteDTO createClienteDTO) {
        Cliente cliente = clienteMapper.toEntity(createClienteDTO);
        Cliente savedCliente = clienteService.save(cliente);
        return ResponseEntity.ok(clienteMapper.toDTO(savedCliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> updateCliente(@PathVariable Long id, @RequestBody UpdateClienteDTO updateClienteDTO) {
        Cliente existingCliente = clienteService.findById(id);
        existingCliente.setNome(updateClienteDTO.nome());
        existingCliente.setEmail(updateClienteDTO.email());
        existingCliente.setTelefone(updateClienteDTO.telefone());
        Cliente updatedCliente = clienteService.save(existingCliente);
        return ResponseEntity.ok(clienteMapper.toDTO(updatedCliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean hardDelete) {
        clienteService.delete(id, hardDelete);
        return ResponseEntity.noContent().build();
    }
}

