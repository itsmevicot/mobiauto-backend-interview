package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Oportunidade.OportunidadeDTO;
import com.mobiauto.backend.application.mappers.OportunidadeMapper;
import com.mobiauto.backend.domain.enums.CargosEnum;
import com.mobiauto.backend.domain.enums.StatusOportunidadeEnum;
import com.mobiauto.backend.domain.models.Oportunidade;
import com.mobiauto.backend.domain.models.Usuario;
import com.mobiauto.backend.domain.repositories.OportunidadeRepository;
import com.mobiauto.backend.domain.repositories.UsuarioRepository;
import com.mobiauto.backend.infrastructure.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class OportunidadeListenerService {

    @Autowired
    private OportunidadeRepository oportunidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OportunidadeMapper oportunidadeMapper;

    @RabbitListener(queues = RabbitMQConfig.OPORTUNIDADE_QUEUE)
    public void receiveMessage(OportunidadeDTO oportunidadeDTO) {
        try {
            System.out.println("Received message: " + oportunidadeDTO);

            Long revendaId = oportunidadeDTO.revendaId();
            CargosEnum cargo = CargosEnum.ASSISTENTE;

            List<Usuario> assistentes = usuarioRepository.findByPerfisCargoNomeAndPerfisRevendaId(cargo, revendaId);

            System.out.println("Assistentes found: " + assistentes.size());

            assistentes.sort(Comparator
                    .comparingInt((Usuario usuario) -> (int) usuario.getOportunidades().stream()
                            .filter(o -> o.getStatus() == StatusOportunidadeEnum.NOVO || o.getStatus() == StatusOportunidadeEnum.EM_ATENDIMENTO)
                            .count())
                    .thenComparing(Usuario::getUltimaOportunidadeRecebida, Comparator.nullsFirst(Comparator.naturalOrder())));

            if (!assistentes.isEmpty()) {
                Usuario assistente = assistentes.get(0);
                Optional<Oportunidade> optionalOportunidade = oportunidadeRepository.findById(oportunidadeDTO.id());
                if (optionalOportunidade.isPresent()) {
                    Oportunidade oportunidade = optionalOportunidade.get();
                    System.out.println("Assigning to assistente: " + assistente.getId());
                    oportunidade.setResponsavelAtendimento(assistente);
                    oportunidade.setDataAtribuicao(LocalDateTime.now());
                    oportunidadeRepository.save(oportunidade);
                    System.out.println("Oportunidade assigned successfully. ResponsavelAtendimentoId: " + oportunidade.getResponsavelAtendimento().getId());
                } else {
                    System.err.println("Oportunidade not found with ID: " + oportunidadeDTO.id());
                }
            } else {
                System.err.println("No assistants available for revenda ID " + revendaId);
                throw new RuntimeException("No assistants available for revenda ID " + revendaId);
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
