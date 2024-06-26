package com.mobiauto.backend.domain.repository;

import com.mobiauto.backend.domain.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    Optional<Veiculo> findByCodigo(String codigo);
}
