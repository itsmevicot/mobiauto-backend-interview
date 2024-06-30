package com.mobiauto.backend.application.mappers;

import com.mobiauto.backend.application.dtos.Cliente.ClienteDTO;
import com.mobiauto.backend.application.dtos.Cliente.CreateClienteDTO;
import com.mobiauto.backend.application.dtos.Cliente.UpdateClienteDTO;
import com.mobiauto.backend.domain.models.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.isAtivo()
        );
    }

    public Cliente toEntity(CreateClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        cliente.setAtivo(true);
        return cliente;
    }

    public Cliente toEntity(UpdateClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        return cliente;
    }
}
