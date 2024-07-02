package com.mobiauto.backend.presentation.controllers;

import com.mobiauto.backend.application.dtos.Oportunidade.OportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.CreateOportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.UpdateOportunidadeDTO;
import com.mobiauto.backend.application.services.OportunidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<List<OportunidadeDTO>> getAllOportunidades() {
        List<OportunidadeDTO> oportunidades = oportunidadeService.findAll();
        return ResponseEntity.ok(oportunidades);
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

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<OportunidadeDTO> finalizarOportunidade(@PathVariable Long id, @RequestParam String motivo, @RequestParam LocalDateTime dataConclusao) {
        OportunidadeDTO oportunidadeDTO = oportunidadeService.finalizarOportunidade(id, motivo, dataConclusao);
        return ResponseEntity.ok(oportunidadeDTO);
    }

    @PostMapping("/{id}/transferir")
    public ResponseEntity<OportunidadeDTO> transferirOportunidade(@PathVariable Long id, @RequestParam Long novoResponsavelId) {
        OportunidadeDTO oportunidadeDTO = oportunidadeService.transferirOportunidade(id, novoResponsavelId);
        return ResponseEntity.ok(oportunidadeDTO);
    }
}
