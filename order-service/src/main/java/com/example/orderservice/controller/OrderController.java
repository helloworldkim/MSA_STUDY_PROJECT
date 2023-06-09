package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
@Slf4j
public class OrderController {

    private final Environment env;
    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;

    @GetMapping("/health_check")
    public String status() {
        return String.format("health_check UserService on PORT : %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(
            @PathVariable("userId") String userId
            ,@RequestBody RequestOrder order) {
        log.info("Before order post data");
        OrderDto orderDto = objectMapper.convertValue(order, OrderDto.class);
        orderDto.setUserId(userId);

        /* JPA */
        orderService.createOrder(orderDto);
        ResponseOrder responseOrder = objectMapper.convertValue(orderDto, ResponseOrder.class);

        /* Kafka producer */
        String topic = "example-catalog-topic";
        kafkaProducer.send(topic,orderDto);
        log.info("After order post data");
        //201 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId) {
        log.info("Before order data");
        List<OrderEntity> orderList = orderService.getOrdersByUserId(userId);
        List<ResponseOrder> result = new ArrayList<>();

        orderList.forEach(o -> result.add(objectMapper.convertValue(o, ResponseOrder.class)));
        log.info("After order data");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }



}
