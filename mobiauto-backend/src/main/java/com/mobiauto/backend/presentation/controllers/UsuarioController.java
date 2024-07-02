package com.mobiauto.backend.presentation.controllers;

import com.mobiauto.backend.application.dtos.Usuario.UsuarioDTO;
import com.mobiauto.backend.application.dtos.Usuario.CreateUsuarioDTO;
import com.mobiauto.backend.application.dtos.Usuario.UpdateUsuarioDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.OportunidadeDTO;
import com.mobiauto.backend.application.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllActiveUsuarios() {
        List<UsuarioDTO> usuariosAtivos = usuarioService.findAllActive();
        return ResponseEntity.ok(usuariosAtivos);
    }

    @GetMapping("/inativos")
    public ResponseEntity<List<UsuarioDTO>> getAllInactiveUsuarios() {
        List<UsuarioDTO> usuariosInativos = usuarioService.findAllInactive();
        return ResponseEntity.ok(usuariosInativos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        UsuarioDTO usuarioDTO = usuarioService.findById(id);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody CreateUsuarioDTO createUsuarioDTO) {
        UsuarioDTO usuarioDTO = usuarioService.createUsuario(createUsuarioDTO);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PostMapping("/{id}/reativar")
    public ResponseEntity<UsuarioDTO> reactivateUsuario(@PathVariable Long id) {
        UsuarioDTO reativadoUsuarioDTO = usuarioService.reativarUsuario(id);
        return ResponseEntity.ok(reativadoUsuarioDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long id, @Valid @RequestBody UpdateUsuarioDTO updateUsuarioDTO) {
        UsuarioDTO updatedUsuarioDTO = usuarioService.updateUsuario(id, updateUsuarioDTO);
        return ResponseEntity.ok(updatedUsuarioDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean hardDelete) {
        usuarioService.delete(id, hardDelete);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/oportunidades")
    public ResponseEntity<List<OportunidadeDTO>> getOportunidadesByUsuario(@PathVariable Long id) {
        List<OportunidadeDTO> oportunidades = usuarioService.findOportunidadesByUsuario(id);
        return ResponseEntity.ok(oportunidades);
    }

}
