package com.example.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

//        @Autowired
//        private RabbitTemplate rabbitTemplate;
//
//        @Bean
//        public RabbitTemplate rabbitTemplate(){
//                rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        }

        @Bean
        public MessageConverter jsonMessageConverter(ObjectMapper objectMapper){
                return new Jackson2JsonMessageConverter(objectMapper);
        }
}
