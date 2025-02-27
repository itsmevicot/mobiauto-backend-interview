package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Oportunidade.OportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.CreateOportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.UpdateOportunidadeDTO;
import com.mobiauto.backend.application.mappers.OportunidadeMapper;
import com.mobiauto.backend.application.utils.AuthorizationUtils;
import com.mobiauto.backend.domain.enums.CargosEnum;
import com.mobiauto.backend.domain.enums.StatusOportunidadeEnum;
import com.mobiauto.backend.domain.exceptions.Auth.UnauthorizedException;
import com.mobiauto.backend.domain.exceptions.Cliente.ClienteNotFoundException;
import com.mobiauto.backend.domain.exceptions.Oportunidade.OportunidadeNotFoundException;
import com.mobiauto.backend.domain.exceptions.Revenda.RevendaNotFoundException;
import com.mobiauto.backend.domain.exceptions.Veiculo.VeiculoNotFoundException;
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
import com.mobiauto.backend.application.utils.CodeGeneratorUtils;
import com.mobiauto.backend.infrastructure.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
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
    private final CodeGeneratorUtils codeGeneratorUtil;
    private final AuthorizationUtils authorizationUtils;
    private final RabbitTemplate rabbitTemplate;
    private final CustomUserDetailsService customUserDetailsService;


    @Autowired
    public OportunidadeService(OportunidadeRepository oportunidadeRepository, OportunidadeMapper oportunidadeMapper,
                               ClienteRepository clienteRepository, RevendaRepository revendaRepository,
                               VeiculoRepository veiculoRepository, UsuarioRepository usuarioRepository,
                               CodeGeneratorUtils codeGeneratorUtil, AuthorizationUtils authorizationUtils,
                               RabbitTemplate rabbitTemplate, CustomUserDetailsService customUserDetailsService) {
        this.oportunidadeRepository = oportunidadeRepository;
        this.oportunidadeMapper = oportunidadeMapper;
        this.clienteRepository = clienteRepository;
        this.revendaRepository = revendaRepository;
        this.veiculoRepository = veiculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.codeGeneratorUtil = codeGeneratorUtil;
        this.authorizationUtils = authorizationUtils;
        this.rabbitTemplate = rabbitTemplate;
        this.customUserDetailsService = customUserDetailsService;
    }

    public List<OportunidadeDTO> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authorizationUtils.isSuperuser(authentication);

        if (isAdmin) {
            return oportunidadeRepository.findAll().stream()
                    .map(oportunidadeMapper::toDTO)
                    .collect(Collectors.toList());
        }

        return oportunidadeRepository.findAll().stream()
                .filter(oportunidade -> authorizationUtils.hasAccessToRevenda(oportunidade.getRevenda().getId()))
                .map(oportunidadeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OportunidadeDTO findById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authorizationUtils.isSuperuser(authentication);

        Oportunidade oportunidade = oportunidadeRepository.findById(id)
                .orElseThrow(OportunidadeNotFoundException::new);

        if (isAdmin || authorizationUtils.hasAccessToRevenda(oportunidade.getRevenda().getId())) {
            return oportunidadeMapper.toDTO(oportunidade);
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public OportunidadeDTO createOportunidade(CreateOportunidadeDTO createOportunidadeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Usuario usuario = (Usuario) customUserDetailsService.loadUserByUsername(email);

        if (usuario.getPerfis().isEmpty()) {
            throw new UnauthorizedException();
        }

        Cliente cliente = clienteRepository.findById(createOportunidadeDTO.clienteId())
                .orElseThrow(ClienteNotFoundException::new);
        Revenda revenda = revendaRepository.findById(createOportunidadeDTO.revendaId())
                .orElseThrow(RevendaNotFoundException::new);
        Veiculo veiculo = veiculoRepository.findById(createOportunidadeDTO.veiculoId())
                .orElseThrow(VeiculoNotFoundException::new);

        Oportunidade oportunidade = oportunidadeMapper.toEntity(createOportunidadeDTO, cliente, revenda, veiculo);
        oportunidade.setCodigo(codeGeneratorUtil.generateOportunidadeCodigo());
        oportunidade.setStatus(StatusOportunidadeEnum.NOVO);
        oportunidade = oportunidadeRepository.save(oportunidade);

        OportunidadeDTO oportunidadeDTO = oportunidadeMapper.toDTO(oportunidade);
        rabbitTemplate.convertAndSend(RabbitMQConfig.OPORTUNIDADE_QUEUE, oportunidadeDTO);

        return oportunidadeDTO;
    }


    @Transactional
    public OportunidadeDTO updateOportunidade(Long id, UpdateOportunidadeDTO updateOportunidadeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        if (!authorizationUtils.canEditOportunidade(usuario, id)) {
            throw new UnauthorizedException();
        }

        Oportunidade existingOportunidade = oportunidadeRepository.findById(id)
                .orElseThrow(OportunidadeNotFoundException::new);

        oportunidadeMapper.updateEntityFromDTO(updateOportunidadeDTO, existingOportunidade);
        existingOportunidade = oportunidadeRepository.save(existingOportunidade);
        return oportunidadeMapper.toDTO(existingOportunidade);
    }


    @Transactional
    public void deleteOportunidade(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Oportunidade oportunidade = oportunidadeRepository.findById(id)
                .orElseThrow(OportunidadeNotFoundException::new);

        boolean isAdmin = authorizationUtils.isSuperuser(authentication);
        boolean isProprietario = authorizationUtils.hasRole(CargosEnum.PROPRIETARIO) &&
                authorizationUtils.hasAccessToRevenda(oportunidade.getRevenda().getId());

        if (!isAdmin && !isProprietario) {
            throw new UnauthorizedException();
        }

        oportunidadeRepository.delete(oportunidade);
    }


    @Transactional
    public OportunidadeDTO finalizarOportunidade(Long oportunidadeId, String motivo, LocalDateTime dataConclusao) {
        if (motivo == null || motivo.isEmpty() || dataConclusao == null) {
            throw new IllegalArgumentException("Motivo e data da conclusão são obrigatórios ao finalizar uma oportunidade.");
        }

        Oportunidade oportunidade = oportunidadeRepository.findById(oportunidadeId)
                .orElseThrow(OportunidadeNotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        boolean isOwner = authorizationUtils.isOwnerOfOportunidade(usuario, oportunidadeId);
        boolean isProprietarioOrGerente = authorizationUtils.hasRole(CargosEnum.PROPRIETARIO) ||
                authorizationUtils.hasRole(CargosEnum.GERENTE);

        if (!isOwner && !isProprietarioOrGerente) {
            throw new UnauthorizedException();
        }

        oportunidade.setStatus(StatusOportunidadeEnum.CONCLUIDO);
        oportunidade.setMotivoConclusao(motivo);
        oportunidade.setDataConclusao(dataConclusao);

        oportunidade = oportunidadeRepository.save(oportunidade);
        return oportunidadeMapper.toDTO(oportunidade);
    }


    @Transactional
    public OportunidadeDTO transferirOportunidade(Long oportunidadeId, Long novoResponsavelId) {
        if (!authorizationUtils.canTransferOportunidade()) {
            throw new UnauthorizedException();
        }

        Oportunidade oportunidade = oportunidadeRepository.findById(oportunidadeId)
                .orElseThrow(OportunidadeNotFoundException::new);

        Usuario novoResponsavel = usuarioRepository.findById(novoResponsavelId)
                .orElseThrow(() -> new IllegalArgumentException("Novo responsável não encontrado"));

        oportunidade.setResponsavelAtendimento(novoResponsavel);

        oportunidade = oportunidadeRepository.save(oportunidade);
        return oportunidadeMapper.toDTO(oportunidade);
    }
}
