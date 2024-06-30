package com.mobiauto.backend.presentation.controllers;

import com.mobiauto.backend.application.dtos.Veiculo.VeiculoDTO;
import com.mobiauto.backend.application.dtos.Veiculo.CreateVeiculoDTO;
import com.mobiauto.backend.application.dtos.Veiculo.UpdateVeiculoDTO;
import com.mobiauto.backend.application.services.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    @Autowired
    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping
    public List<VeiculoDTO> getAllVeiculos() {
        return veiculoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> getVeiculoById(@PathVariable Long id) {
        VeiculoDTO veiculoDTO = veiculoService.findById(id);
        return ResponseEntity.ok(veiculoDTO);
    }

    @PostMapping
    public ResponseEntity<VeiculoDTO> createVeiculo(@Valid @RequestBody CreateVeiculoDTO createVeiculoDTO) {
        VeiculoDTO veiculoDTO = veiculoService.createVeiculo(createVeiculoDTO);
        return ResponseEntity.ok(veiculoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoDTO> updateVeiculo(@PathVariable Long id, @Valid @RequestBody UpdateVeiculoDTO updateVeiculoDTO) {
        VeiculoDTO veiculoDTO = veiculoService.updateVeiculo(id, updateVeiculoDTO);
        return ResponseEntity.ok(veiculoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeiculo(@PathVariable Long id) {
        veiculoService.deleteVeiculo(id);
        return ResponseEntity.noContent().build();
    }
}
