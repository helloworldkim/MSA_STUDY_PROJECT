package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty()* orderDto.getUnitPrice());

        OrderEntity orderEntity = objectMapper.convertValue(orderDto, OrderEntity.class);


        orderRepository.save(orderEntity);

        OrderDto returnUserdto = objectMapper.convertValue(orderEntity, OrderDto.class);
        return returnUserdto;
    }
    public OrderDto getOrdersByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId).orElseThrow();
        OrderDto returnUserdto = objectMapper.convertValue(orderEntity, OrderDto.class);
        return returnUserdto;
    }
    public List<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }



}
