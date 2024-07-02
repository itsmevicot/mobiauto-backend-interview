package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.enums.CargosEnum;
import com.mobiauto.backend.domain.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findAllByAtivoTrue();
    List<Usuario> findAllByAtivoFalse();
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(u.codigo, 4) AS int)), 0) FROM Usuario u")
    int findMaxCodigo();

    @Query("SELECT u FROM Usuario u JOIN u.perfis p WHERE u.ativo = true AND p.revenda.id = :revendaId")
    List<Usuario> findAllByAtivoTrueAndRevendaId(@Param("revendaId") Long revendaId);

    @Query("SELECT u FROM Usuario u JOIN u.perfis p WHERE u.ativo = false AND p.revenda.id = :revendaId")
    List<Usuario> findAllByAtivoFalseAndRevendaId(@Param("revendaId") Long revendaId);

    @Query("SELECT u FROM Usuario u JOIN u.perfis p WHERE p.revenda.id = :revendaId AND p.cargo.nome = :role")
    List<Usuario> findAllByRevendaAndRole(Long revendaId, CargosEnum role);

    @Query("SELECT u FROM Usuario u JOIN u.perfis p WHERE p.cargo.nome = :cargoNome AND p.revenda.id = :revendaId")
    List<Usuario> findByPerfisCargoNomeAndPerfisRevendaId(String cargoNome, Long revendaId);
}
