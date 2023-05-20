package com.targa.labs.services;

import com.targa.labs.models.Order;
import com.targa.labs.models.OrderItem;
import com.targa.labs.models.Product;
import com.targa.labs.repositories.OrderItemRepository;
import com.targa.labs.repositories.OrderRepository;
import com.targa.labs.repositories.ProductRepository;
import com.targa.labs.services.dtos.OrderItemDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
@AllArgsConstructor
public class OrderItemService {

    private OrderItemRepository orderItemRepository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    public OrderItemDto findById(Long id){
        log.debug("Request to get OrderItem : {}", id);
        return orderItemRepository.findById(id)
                .map(OrderItemDto::mapToDto)
                .orElse(null);
    }

    public OrderItemDto create(OrderItemDto orderItemDto){
        log.debug("Request to create OrderItem : {}", orderItemDto);

        Order order = orderRepository.findById(orderItemDto.getOrderId())
                .orElseThrow(() -> new IllegalStateException("The Order does not exist!"));
        Product product = productRepository.findById(orderItemDto.getProductId())
                .orElseThrow(() -> new IllegalStateException("The Product does not exist!"));

        OrderItem orderItem = orderItemRepository.save(new OrderItem(orderItemDto.getQuantity(), product, order));
        order.setPrice(order.getPrice().add(orderItem.getProduct().getPrice()));
        orderRepository.save(order);

        return OrderItemDto.mapToDto(orderItem);
    }

    public void delete(Long id){
        log.debug("Request to delete OrderItem : {}", id);

        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("The OrderItem does not exist!"));
        Order order = orderItem.getOrder();

        order.setPrice(order.getPrice().subtract(orderItem.getProduct().getPrice()));
        orderItemRepository.deleteById(id);
        order.getOrderItems().remove(orderItem);
        orderRepository.save(order);
    }

    public List<OrderItemDto> findByOrderId(Long id){
        log.debug("Request to get all OrderItems of OrderId {}", id);

        return orderItemRepository.findAllByOrderId(id)
                .stream()
                .map(OrderItemDto::mapToDto)
                .collect(Collectors.toList());
    }
}
