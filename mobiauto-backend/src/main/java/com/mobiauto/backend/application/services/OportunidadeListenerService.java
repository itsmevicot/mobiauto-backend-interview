package com.mobiauto.backend.application.services;

import com.mobiauto.backend.application.dtos.Oportunidade.OportunidadeDTO;
import com.mobiauto.backend.infrastructure.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OportunidadeListenerService {

    @Autowired
    private OportunidadeService oportunidadeService;

    @RabbitListener(queues = RabbitMQConfig.OPORTUNIDADE_QUEUE)
    public void listen(OportunidadeDTO oportunidadeDTO) {
        oportunidadeService.distributeOportunidade(oportunidadeDTO);
    }
}
