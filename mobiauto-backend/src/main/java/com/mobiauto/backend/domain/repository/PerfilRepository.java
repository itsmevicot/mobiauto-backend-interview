package com.mobiauto.backend.domain.repository;

import com.mobiauto.backend.domain.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
}