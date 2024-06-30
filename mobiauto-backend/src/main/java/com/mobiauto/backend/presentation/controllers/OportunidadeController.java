package com.mobiauto.backend.presentation.controllers;

import com.mobiauto.backend.application.services.OportunidadeService;
import com.mobiauto.backend.domain.models.Oportunidade;
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
    public List<Oportunidade> getAllOportunidades() {
        return oportunidadeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Oportunidade> getOportunidadeById(@PathVariable Long id) {
        Oportunidade oportunidade = oportunidadeService.findById(id);
        return ResponseEntity.ok(oportunidade);
    }

    @PostMapping
    public Oportunidade createOportunidade(@RequestBody Oportunidade oportunidade) {
        return oportunidadeService.save(oportunidade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Oportunidade> updateOportunidade(@PathVariable Long id, @RequestBody Oportunidade oportunidadeDetails) {
        Oportunidade oportunidade = oportunidadeService.findById(id);
        oportunidade.setCodigo(oportunidadeDetails.getCodigo());
        oportunidade.setStatus(oportunidadeDetails.getStatus());
        oportunidade.setMotivoConclusao(oportunidadeDetails.getMotivoConclusao());
        oportunidade.setDataAtribuicao(oportunidadeDetails.getDataAtribuicao());
        oportunidade.setDataConclusao(oportunidadeDetails.getDataConclusao());
        oportunidade.setCliente(oportunidadeDetails.getCliente());
        oportunidade.setRevenda(oportunidadeDetails.getRevenda());
        oportunidade.setVeiculo(oportunidadeDetails.getVeiculo());
        oportunidade.setResponsavelAtendimento(oportunidadeDetails.getResponsavelAtendimento());
        final Oportunidade updatedOportunidade = oportunidadeService.save(oportunidade);
        return ResponseEntity.ok(updatedOportunidade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOportunidade(@PathVariable Long id) {
        oportunidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

