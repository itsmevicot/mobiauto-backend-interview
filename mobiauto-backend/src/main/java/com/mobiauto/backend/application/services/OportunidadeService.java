package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Oportunidade.OportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.CreateOportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.UpdateOportunidadeDTO;
import com.mobiauto.backend.application.mappers.OportunidadeMapper;
import com.mobiauto.backend.domain.exceptions.Oportunidade.OportunidadeNotFoundException;
import com.mobiauto.backend.domain.models.Oportunidade;
import com.mobiauto.backend.domain.models.Cliente;
import com.mobiauto.backend.domain.models.Revenda;
import com.mobiauto.backend.domain.models.Veiculo;
import com.mobiauto.backend.domain.models.Usuario;
import com.mobiauto.backend.domain.repositories.OportunidadeRepository;
import com.mobiauto.backend.domain.repositories.ClienteRepository;
import com.mobiauto.backend.domain.repositories.RevendaRepository;
import com.mobiauto.backend.domain.repositories.VeiculoRepository;
import com.mobiauto.backend.domain.repositories.UsuarioRepository;
import com.mobiauto.backend.domain.utils.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OportunidadeService {

    private final OportunidadeRepository oportunidadeRepository;
    private final OportunidadeMapper oportunidadeMapper;
    private final ClienteRepository clienteRepository;
    private final RevendaRepository revendaRepository;
    private final VeiculoRepository veiculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CodeGenerator codeGenerator;

    @Autowired
    public OportunidadeService(OportunidadeRepository oportunidadeRepository, OportunidadeMapper oportunidadeMapper,
                               ClienteRepository clienteRepository, RevendaRepository revendaRepository,
                               VeiculoRepository veiculoRepository, UsuarioRepository usuarioRepository,
                               CodeGenerator codeGenerator) {
        this.oportunidadeRepository = oportunidadeRepository;
        this.oportunidadeMapper = oportunidadeMapper;
        this.clienteRepository = clienteRepository;
        this.revendaRepository = revendaRepository;
        this.veiculoRepository = veiculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.codeGenerator = codeGenerator;
    }

    public List<OportunidadeDTO> findAll() {
        return oportunidadeRepository.findAll().stream()
                .map(oportunidadeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OportunidadeDTO findById(Long id) {
        Oportunidade oportunidade = oportunidadeRepository.findById(id)
                .orElseThrow(OportunidadeNotFoundException::new);
        return oportunidadeMapper.toDTO(oportunidade);
    }

    @Transactional
    public OportunidadeDTO createOportunidade(CreateOportunidadeDTO createOportunidadeDTO) {
        Cliente cliente = clienteRepository.findById(createOportunidadeDTO.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente not found")); // Create a custom exception for this if needed
        Revenda revenda = revendaRepository.findById(createOportunidadeDTO.revendaId())
                .orElseThrow(() -> new RuntimeException("Revenda not found")); // Create a custom exception for this if needed
        Veiculo veiculo = veiculoRepository.findById(createOportunidadeDTO.veiculoId())
                .orElseThrow(() -> new RuntimeException("Veiculo not found")); // Create a custom exception for this if needed
        Usuario responsavelAtendimento = usuarioRepository.findById(createOportunidadeDTO.responsavelAtendimentoId())
                .orElseThrow(() -> new RuntimeException("Usuario not found")); // Create a custom exception for this if needed

        Oportunidade oportunidade = oportunidadeMapper.toEntity(createOportunidadeDTO, cliente, revenda, veiculo, responsavelAtendimento);
        oportunidade.setCodigo(codeGenerator.generateOportunidadeCodigo());
        oportunidade = oportunidadeRepository.save(oportunidade);
        return oportunidadeMapper.toDTO(oportunidade);
    }

    @Transactional
    public OportunidadeDTO updateOportunidade(Long id, UpdateOportunidadeDTO updateOportunidadeDTO) {
        Oportunidade existingOportunidade = oportunidadeRepository.findById(id)
                .orElseThrow(OportunidadeNotFoundException::new);

        Cliente cliente = clienteRepository.findById(updateOportunidadeDTO.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente not found")); // Create a custom exception for this if needed
        Revenda revenda = revendaRepository.findById(updateOportunidadeDTO.revendaId())
                .orElseThrow(() -> new RuntimeException("Revenda not found")); // Create a custom exception for this if needed
        Veiculo veiculo = veiculoRepository.findById(updateOportunidadeDTO.veiculoId())
                .orElseThrow(() -> new RuntimeException("Veiculo not found")); // Create a custom exception for this if needed
        Usuario responsavelAtendimento = usuarioRepository.findById(updateOportunidadeDTO.responsavelAtendimentoId())
                .orElseThrow(() -> new RuntimeException("Usuario not found")); // Create a custom exception for this if needed

        oportunidadeMapper.updateEntityFromDTO(updateOportunidadeDTO, existingOportunidade, cliente, revenda, veiculo, responsavelAtendimento);
        existingOportunidade = oportunidadeRepository.save(existingOportunidade);
        return oportunidadeMapper.toDTO(existingOportunidade);
    }

    @Transactional
    public void deleteOportunidade(Long id) {
        Oportunidade oportunidade = oportunidadeRepository.findById(id)
                .orElseThrow(OportunidadeNotFoundException::new);
        oportunidadeRepository.delete(oportunidade);
    }
}
