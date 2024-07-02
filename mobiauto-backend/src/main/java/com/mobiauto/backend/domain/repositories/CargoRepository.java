package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.models.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
}
