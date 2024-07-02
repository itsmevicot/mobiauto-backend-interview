package com.mobiauto.backend.presentation.controllers;

import com.mobiauto.backend.application.dtos.Perfil.PerfilDTO;
import com.mobiauto.backend.application.dtos.Perfil.CreatePerfilDTO;
import com.mobiauto.backend.application.dtos.Perfil.UpdatePerfilDTO;
import com.mobiauto.backend.application.services.PerfilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/perfis")
public class PerfilController {

    private final PerfilService perfilService;

    @Autowired
    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    public List<PerfilDTO> getAllPerfis() {
        return perfilService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilDTO> getPerfilById(@PathVariable Long id) {
        PerfilDTO perfilDTO = perfilService.findById(id);
        return ResponseEntity.ok(perfilDTO);
    }

    @PostMapping
    public ResponseEntity<PerfilDTO> createPerfil(@Valid @RequestBody CreatePerfilDTO createPerfilDTO) {
        PerfilDTO perfilDTO = perfilService.createPerfil(createPerfilDTO);
        return ResponseEntity.ok(perfilDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilDTO> updatePerfil(@PathVariable Long id, @Valid @RequestBody UpdatePerfilDTO updatePerfilDTO) {
        PerfilDTO perfilDTO = perfilService.updatePerfil(id, updatePerfilDTO);
        return ResponseEntity.ok(perfilDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerfil(@PathVariable Long id) {
        perfilService.deletePerfil(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/revenda/{revendaId}")
    public ResponseEntity<List<PerfilDTO>> findByRevenda(@PathVariable Long revendaId) {
        List<PerfilDTO> perfis = perfilService.findByRevenda(revendaId);
        return ResponseEntity.ok(perfis);
    }
}
