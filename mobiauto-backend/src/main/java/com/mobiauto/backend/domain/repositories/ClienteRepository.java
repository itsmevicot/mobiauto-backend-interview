package com.mobiauto.backend.domain.repositories;

import com.mobiauto.backend.domain.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findAllByAtivoTrue();
    List<Cliente> findAllByAtivoFalse();
    Cliente findByEmail(String email);
    boolean existsByEmail(String email);
}
