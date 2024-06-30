package com.mobiauto.backend.application.services;

import com.mobiauto.backend.domain.exceptions.Cargo.CargoNotFoundException;
import com.mobiauto.backend.domain.models.Cargo;
import com.mobiauto.backend.domain.repositories.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoService {

    private final CargoRepository cargoRepository;

    @Autowired
    public CargoService(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    public Cargo findById(Long id) {
        return cargoRepository.findById(id).orElseThrow((CargoNotFoundException::new));
    }

    public Cargo save(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    public void delete(Long id) {
        Cargo cargo = findById(id);
        cargoRepository.delete(cargo);
    }
}