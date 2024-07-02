package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.models.Oportunidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {
    List<Oportunidade> findByResponsavelAtendimentoId(Long usuarioId);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(o.codigo, 4) AS int)), 0) FROM Oportunidade o")
    int findMaxCodigo();
}
