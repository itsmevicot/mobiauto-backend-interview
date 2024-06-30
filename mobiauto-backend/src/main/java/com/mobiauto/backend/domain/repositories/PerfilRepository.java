package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.models.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
}