package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.models.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    List<Veiculo> findByRevendaId(Long revendaId);
}
