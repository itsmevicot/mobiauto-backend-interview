package com.mobiauto.backend.application.services;

import com.mobiauto.backend.domain.models.Oportunidade;
import com.mobiauto.backend.domain.repositories.OportunidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OportunidadeService {

    private final OportunidadeRepository oportunidadeRepository;

    @Autowired
    public OportunidadeService(OportunidadeRepository oportunidadeRepository) {
        this.oportunidadeRepository = oportunidadeRepository;
    }

    public List<Oportunidade> findAll() {
        return oportunidadeRepository.findAll();
    }

    public Oportunidade findById(Long id) {
        return oportunidadeRepository.findById(id).orElseThrow(() -> new RuntimeException("Oportunidade not found"));
    }

    public Oportunidade save(Oportunidade oportunidade) {
        return oportunidadeRepository.save(oportunidade);
    }

    public void delete(Long id) {
        Oportunidade oportunidade = findById(id);
        oportunidadeRepository.delete(oportunidade);
    }
}
