package com.example.orderservice.messagequeue;

import com.example.orderservice.dto.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderDto send(String topic, OrderDto orderDto) {

        String jsonInString = "";

        try {
            jsonInString = objectMapper.writeValueAsString(orderDto);
        } catch (JsonProcessingException e) {
            log.error("Error JsonProcessingException Kafka Message : {}", e.getMessage());
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Message send date from Order Service : {}", jsonInString);

        return orderDto;

    }
}
