package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.utils.AuthorizationUtils;
import com.mobiauto.backend.domain.exceptions.Auth.UnauthorizedException;
import com.mobiauto.backend.domain.exceptions.Cargo.CargoNotFoundException;
import com.mobiauto.backend.domain.models.Cargo;
import com.mobiauto.backend.domain.repositories.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CargoService {

    private final CargoRepository cargoRepository;
    private final AuthorizationUtils authorizationUtils;

    @Autowired
    public CargoService(CargoRepository cargoRepository, AuthorizationUtils authorizationUtils) {
        this.cargoRepository = cargoRepository;
        this.authorizationUtils = authorizationUtils;
    }

    public Cargo findById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authorizationUtils.isSuperuser(authentication)) {
            throw new UnauthorizedException();
        }
        return cargoRepository.findById(id).orElseThrow(CargoNotFoundException::new);
    }

    @Transactional
    public Cargo save(Cargo cargo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authorizationUtils.isSuperuser(authentication)) {
            throw new UnauthorizedException();
        }
        return cargoRepository.save(cargo);
    }

    @Transactional
    public void delete(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authorizationUtils.isSuperuser(authentication)) {
            throw new UnauthorizedException();
        }
        Cargo cargo = findById(id);
        cargoRepository.delete(cargo);
    }
}
