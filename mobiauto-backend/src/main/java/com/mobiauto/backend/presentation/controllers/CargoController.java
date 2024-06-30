package com.mobiauto.backend.presentation.controllers;

import com.mobiauto.backend.application.services.CargoService;
import com.mobiauto.backend.domain.models.Cargo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cargos")
public class CargoController {

    private final CargoService cargoService;

    @Autowired
    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @GetMapping
    public List<Cargo> getAllActiveCargos() {
        return cargoService.findAllActive();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cargo> getCargoById(@PathVariable Long id) {
        Cargo cargo = cargoService.findById(id);
        return ResponseEntity.ok(cargo);
    }

    @PostMapping
    public Cargo createCargo(@RequestBody Cargo cargo) {
        return cargoService.save(cargo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cargo> updateCargo(@PathVariable Long id, @RequestBody Cargo cargoDetails) {
        Cargo cargo = cargoService.findById(id);
        cargo.setNome(cargoDetails.getNome());
        cargo.setPermissoes(cargoDetails.getPermissoes());
        cargo.setPerfis(cargoDetails.getPerfis());
        final Cargo updatedCargo = cargoService.save(cargo);
        return ResponseEntity.ok(updatedCargo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCargo(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean hardDelete) {
        cargoService.delete(id, hardDelete);
        return ResponseEntity.noContent().build();
    }
}
