package com.example.catalogservice.messagequeue;

import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.jpa.CatalogRepository;
import com.example.catalogservice.service.CatalogService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CatalogRepository repository;
    private final ObjectMapper objectMapper;


    @KafkaListener(topics = "example-catalog-topic")
    public void updateQty(String kafkaMessage) {
        log.info("kafka Meassage : {}", kafkaMessage);

        HashMap<Object, Object> map = new HashMap<>();

        try {
            map = objectMapper.readValue(kafkaMessage, new TypeReference<HashMap<Object, Object>>() {});
        } catch (Exception e) {
            log.error("Error reading Kafka Message : {}", e.getMessage());
            e.printStackTrace();
        }

        Optional<CatalogEntity> entityOptional = repository.findByProductId((String) map.get("productId"));

        if (entityOptional.isPresent()) {
            CatalogEntity entity = entityOptional.get();
            entity.setStock(entity.getStock() - (Integer) map.get("qty"));
            repository.save(entity);
        }


    }
}
