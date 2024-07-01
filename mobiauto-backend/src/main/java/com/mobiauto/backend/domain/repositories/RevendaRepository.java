package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.models.Revenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RevendaRepository extends JpaRepository<Revenda, Long> {
    Optional<Revenda> findByCnpj(String cnpj);
    Optional<Revenda> findByCodigo(String codigo);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(r.codigo, 4) AS int)), 0) FROM Revenda r")
    int findMaxCodigo();
}
