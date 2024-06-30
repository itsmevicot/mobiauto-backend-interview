package com.mobiauto.backend.presentation.controllers;

import com.mobiauto.backend.application.dtos.Oportunidade.OportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.CreateOportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.UpdateOportunidadeDTO;
import com.mobiauto.backend.application.services.OportunidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/oportunidades")
public class OportunidadeController {

    private final OportunidadeService oportunidadeService;

    @Autowired
    public OportunidadeController(OportunidadeService oportunidadeService) {
        this.oportunidadeService = oportunidadeService;
    }

    @GetMapping
    public List<OportunidadeDTO> getAllOportunidades() {
        return oportunidadeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OportunidadeDTO> getOportunidadeById(@PathVariable Long id) {
        OportunidadeDTO oportunidadeDTO = oportunidadeService.findById(id);
        return ResponseEntity.ok(oportunidadeDTO);
    }

    @PostMapping
    public ResponseEntity<OportunidadeDTO> createOportunidade(@Valid @RequestBody CreateOportunidadeDTO createOportunidadeDTO) {
        OportunidadeDTO oportunidadeDTO = oportunidadeService.createOportunidade(createOportunidadeDTO);
        return ResponseEntity.ok(oportunidadeDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OportunidadeDTO> updateOportunidade(@PathVariable Long id, @Valid @RequestBody UpdateOportunidadeDTO updateOportunidadeDTO) {
        OportunidadeDTO oportunidadeDTO = oportunidadeService.updateOportunidade(id, updateOportunidadeDTO);
        return ResponseEntity.ok(oportunidadeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOportunidade(@PathVariable Long id) {
        oportunidadeService.deleteOportunidade(id);
        return ResponseEntity.noContent().build();
    }
}
