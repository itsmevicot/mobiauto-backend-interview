package com.mobiauto.backend.application.services;

import com.mobiauto.backend.domain.exceptions.Cargo.CargoNotFoundException;
import com.mobiauto.backend.domain.models.Cargo;
import com.mobiauto.backend.domain.repositories.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class CargoService {

    private final CargoRepository cargoRepository;

    @Autowired
    public CargoService(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Cargo findById(Long id) {
        return cargoRepository.findById(id).orElseThrow(CargoNotFoundException::new);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Cargo save(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        Cargo cargo = findById(id);
        cargoRepository.delete(cargo);
    }
}
