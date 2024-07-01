package com.mobiauto.backend.domain.utils;

import com.mobiauto.backend.domain.repositories.UsuarioRepository;
import com.mobiauto.backend.domain.repositories.RevendaRepository;
import com.mobiauto.backend.domain.repositories.VeiculoRepository;
import com.mobiauto.backend.domain.repositories.OportunidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodeGeneratorUtil {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RevendaRepository revendaRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private OportunidadeRepository oportunidadeRepository;

    public String generateUsuarioCodigo() {
        int maxCodigo = usuarioRepository.findMaxCodigo();
        return "USU" + String.format("%03d", maxCodigo + 1);
    }

    public String generateRevendaCodigo() {
        int maxCodigo = revendaRepository.findMaxCodigo();
        return "REV" + String.format("%03d", maxCodigo + 1);
    }

    public String generateVeiculoCodigo() {
        int maxCodigo = veiculoRepository.findMaxCodigo();
        return "VEI" + String.format("%03d", maxCodigo + 1);
    }

    public String generateOportunidadeCodigo() {
        int maxCodigo = oportunidadeRepository.findMaxCodigo();
        return "OPO" + String.format("%03d", maxCodigo + 1);
    }
}
