package com.mobiauto.backend.presentation.controllers;

import com.mobiauto.backend.domain.models.Cargo;
import com.mobiauto.backend.application.services.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cargos")
public class CargoController {

    private final CargoService cargoService;

    @Autowired
    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Cargo>> getAllCargos() {
        Iterable<Cargo> cargos = cargoService.findAll();
        return ResponseEntity.ok(cargos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cargo> getCargoById(@PathVariable Long id) {
        Cargo cargo = cargoService.findById(id);
        return ResponseEntity.ok(cargo);
    }

    @PostMapping
    public ResponseEntity<Cargo> createCargo(@RequestBody Cargo cargo) {
        Cargo createdCargo = cargoService.save(cargo);
        return ResponseEntity.ok(createdCargo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cargo> updateCargo(@PathVariable Long id, @RequestBody Cargo cargo) {
        cargo.setId(id); // Ensure the ID is set correctly
        Cargo updatedCargo = cargoService.save(cargo);
        return ResponseEntity.ok(updatedCargo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCargo(@PathVariable Long id) {
        cargoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
