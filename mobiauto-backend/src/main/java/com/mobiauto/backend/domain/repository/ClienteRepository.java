package com.mobiauto.backend.domain.repository;

import com.mobiauto.backend.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
