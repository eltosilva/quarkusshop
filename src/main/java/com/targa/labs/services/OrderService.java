package com.targa.labs.services;

import com.targa.labs.models.Cart;
import com.targa.labs.models.Order;
import com.targa.labs.models.OrderStatus;
import com.targa.labs.repositories.CartRepository;
import com.targa.labs.repositories.OrderRepository;
import com.targa.labs.repositories.PaymentRepository;
import com.targa.labs.services.dtos.AddressDto;
import com.targa.labs.services.dtos.OrderDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
@AllArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;
    private PaymentRepository paymentRepository;
    private CartRepository cartRepository;

    public List<OrderDto> findAll() {
        log.debug("Request to get all Orders");

        return orderRepository.findAll()
                .stream()
                .map(OrderDto::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto findById(Long id){
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id)
                .map(OrderDto::mapToDto)
                .orElse(null);
    }

    public List<OrderDto> findAllByUser(Long customerId) {
        return orderRepository.findByCartCustomerId(customerId)
                .stream()
                .map(OrderDto::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto create(OrderDto orderDto) {
        log.debug("Request to create Order : {}", orderDto);

        Long cartId = orderDto.getCart().getId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalStateException("The Cart with ID[" + cartId + "] was not found !"));

        Order order = orderRepository.save(new Order(
                BigDecimal.ZERO, OrderStatus.CREATION, null, null,
                AddressDto.mapToModel(orderDto.getShipmentAddress()), Collections.emptySet(), cart
                )
        );

        return OrderDto.mapToDto(order);
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Order with ID[" + id + "] cannot be found!"));

        Optional.ofNullable(order.getPayment())
                .ifPresent(paymentRepository::delete);
        orderRepository.delete(order);
    }

    public boolean existsById(Long id){
        return orderRepository.existsById(id);
    }
}
