package com.mobiauto.backend.domain.repository;


import com.mobiauto.backend.domain.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
    Optional<Permissao> findByDescricao(String descricao);
}