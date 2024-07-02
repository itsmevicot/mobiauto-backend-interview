package com.mobiauto.backend.presentation.controllers;

import com.mobiauto.backend.application.dtos.Cliente.ClienteDTO;
import com.mobiauto.backend.application.dtos.Cliente.CreateClienteDTO;
import com.mobiauto.backend.application.dtos.Cliente.UpdateClienteDTO;
import com.mobiauto.backend.application.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAllActiveClientes() {
        return ResponseEntity.ok(clienteService.findAllActive());
    }

    @GetMapping("/inativos")
    public ResponseEntity<List<ClienteDTO>> getAllInactiveClientes() {
        return ResponseEntity.ok(clienteService.findAllInactive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        ClienteDTO clienteDTO = clienteService.findById(id);
        return ResponseEntity.ok(clienteDTO);
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> createCliente(@Valid @RequestBody CreateClienteDTO createClienteDTO) {
        ClienteDTO clienteDTO = clienteService.createCliente(createClienteDTO);
        return ResponseEntity.ok(clienteDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> updateCliente(@PathVariable Long id, @Valid @RequestBody UpdateClienteDTO updateClienteDTO) {
        ClienteDTO updatedClienteDTO = clienteService.updateCliente(id, updateClienteDTO);
        return ResponseEntity.ok(updatedClienteDTO);
    }

    @PostMapping("/{id}/reativar")
    public ResponseEntity<ClienteDTO> reactivateCliente(@PathVariable Long id) {
        ClienteDTO reativadoClienteDTO = clienteService.reativarCliente(id);
        return ResponseEntity.ok(reativadoClienteDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean hardDelete) {
        clienteService.delete(id, hardDelete);
        return ResponseEntity.noContent().build();
    }
}
