package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findAllByAtivoTrue();
    List<Usuario> findAllByAtivoFalse();
    Usuario findByEmail(String email);
    boolean existsByEmail(String email);

}
