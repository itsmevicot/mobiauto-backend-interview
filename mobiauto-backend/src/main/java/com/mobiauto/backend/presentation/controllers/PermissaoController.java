package com.mobiauto.backend.presentation.controllers;

import com.mobiauto.backend.application.dtos.Permissao.PermissaoDTO;
import com.mobiauto.backend.application.dtos.Permissao.CreatePermissaoDTO;
import com.mobiauto.backend.application.dtos.Permissao.UpdatePermissaoDTO;
import com.mobiauto.backend.application.services.PermissaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissoes")
public class PermissaoController {

    private final PermissaoService permissaoService;

    @Autowired
    public PermissaoController(PermissaoService permissaoService) {
        this.permissaoService = permissaoService;
    }

    @GetMapping
    public List<PermissaoDTO> getAllPermissoes() {
        return permissaoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissaoDTO> getPermissaoById(@PathVariable Long id) {
        PermissaoDTO permissaoDTO = permissaoService.findById(id);
        return ResponseEntity.ok(permissaoDTO);
    }

    @PostMapping
    public ResponseEntity<PermissaoDTO> createPermissao(@Valid @RequestBody CreatePermissaoDTO createPermissaoDTO) {
        PermissaoDTO permissaoDTO = permissaoService.createPermissao(createPermissaoDTO);
        return ResponseEntity.ok(permissaoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissaoDTO> updatePermissao(@PathVariable Long id, @Valid @RequestBody UpdatePermissaoDTO updatePermissaoDTO) {
        PermissaoDTO permissaoDTO = permissaoService.updatePermissao(id, updatePermissaoDTO);
        return ResponseEntity.ok(permissaoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermissao(@PathVariable Long id) {
        permissaoService.deletePermissao(id);
        return ResponseEntity.noContent().build();
    }
}
