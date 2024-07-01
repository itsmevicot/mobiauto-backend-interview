package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findAllByAtivoTrue();
    List<Usuario> findAllByAtivoFalse();
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(u.codigo, 4) AS int)), 0) FROM Usuario u")
    int findMaxCodigo();
}
