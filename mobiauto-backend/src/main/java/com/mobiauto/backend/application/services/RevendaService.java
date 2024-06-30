package com.mobiauto.backend.application.services;

import com.mobiauto.backend.domain.models.Revenda;
import com.mobiauto.backend.domain.repositories.RevendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class RevendaService {
    @Autowired
    private RevendaRepository revendaRepository;

    public List<Revenda> findAll() {
        return revendaRepository.findAll();
    }

    public Optional<Revenda> findById(Long id) {
        return revendaRepository.findById(id);
    }

    public Revenda save(Revenda revenda) {
        return revendaRepository.save(revenda);
    }

    public void deleteById(Long id) {
        revendaRepository.deleteById(id);
    }
}