package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.models.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    List<Perfil> findByRevendaId(Long revendaId);
}