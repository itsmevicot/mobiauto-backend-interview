package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.models.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    Optional<Veiculo> findByCodigo(String codigo);
    List<Veiculo> findByRevendaId(Long revendaId);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(v.codigo, 4) AS int)), 0) FROM Veiculo v")
    int findMaxCodigo();
}
