package com.mobiauto.backend.presentation.controllers;

import com.mobiauto.backend.application.dtos.Revenda.RevendaDTO;
import com.mobiauto.backend.application.dtos.Revenda.CreateRevendaDTO;
import com.mobiauto.backend.application.dtos.Revenda.UpdateRevendaDTO;
import com.mobiauto.backend.application.services.RevendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/revendas")
public class RevendaController {

    private final RevendaService revendaService;

    @Autowired
    public RevendaController(RevendaService revendaService) {
        this.revendaService = revendaService;
    }

    @GetMapping
    public List<RevendaDTO> getAllRevendas() {
        return revendaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RevendaDTO> getRevendaById(@PathVariable Long id) {
        RevendaDTO revendaDTO = revendaService.findById(id);
        return ResponseEntity.ok(revendaDTO);
    }

    @PostMapping
    public ResponseEntity<RevendaDTO> createRevenda(@Valid @RequestBody CreateRevendaDTO createRevendaDTO) {
        RevendaDTO revendaDTO = revendaService.createRevenda(createRevendaDTO);
        return ResponseEntity.ok(revendaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RevendaDTO> updateRevenda(@PathVariable Long id, @Valid @RequestBody UpdateRevendaDTO updateRevendaDTO) {
        RevendaDTO revendaDTO = revendaService.updateRevenda(id, updateRevendaDTO);
        return ResponseEntity.ok(revendaDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRevenda(@PathVariable Long id) {
        revendaService.deleteRevenda(id);
        return ResponseEntity.noContent().build();
    }
}
