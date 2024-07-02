package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.models.Cliente;
import com.mobiauto.backend.domain.models.Oportunidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(o.codigo, 4) AS int)), 0) FROM Oportunidade o")
    int findMaxCodigo();

    @Query("SELECT DISTINCT c FROM Cliente c JOIN Oportunidade o ON c.id = o.cliente.id WHERE o.revenda.id = :revendaId AND c.ativo = :ativo")
    List<Cliente> findClientesByRevendaIdAndAtivo(Long revendaId, boolean ativo);

    boolean existsByClienteIdAndRevendaId(Long clienteId, Long revendaId);
}
