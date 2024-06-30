package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findAllByAtivoTrue();
    boolean existsByEmail(String email);
}
