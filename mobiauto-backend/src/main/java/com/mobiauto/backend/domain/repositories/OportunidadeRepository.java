package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.models.Oportunidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {
    Optional<Oportunidade> findByCodigo(String codigo);
}
