package com.mobiauto.backend.infrastructure.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String OPORTUNIDADE_QUEUE = "oportunidade_queue";

    @Bean
    public Queue oportunidadeQueue() {
        return new Queue(OPORTUNIDADE_QUEUE, true);
    }
}
