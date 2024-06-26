package com.mobiauto.backend.domain.repository;

import com.mobiauto.backend.domain.model.Revenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RevendaRepository extends JpaRepository<Revenda, Long> {
    Optional<Revenda> findByCnpj(String cnpj);
    Optional<Revenda> findByCodigo(String codigo);
}
