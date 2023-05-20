package com.targa.labs.services.dtos;

import com.targa.labs.models.Order;
import com.targa.labs.services.OrderItemService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private BigDecimal totalPrice;
    private String status;
    private ZonedDateTime shipped;
    private Long paymentId;
    private AddressDto shipmentAddress;
    private Set<OrderItemDto> orderItems;
    private CartDto cart;

    public static OrderDto mapToDto(Order order){
        Set<OrderItemDto> orderItems = order.getOrderItems()
                .stream()
                .map(OrderItemDto::mapToDto)
                .collect(Collectors.toSet());

        Long paymentId = order.getPayment() != null ? order.getPayment().getId() : null;
        return new OrderDto(
                order.getId(),
                order.getPrice(),
                order.getStatus().name(),
                order.getShipped(),
                paymentId,
                AddressDto.mapToDto(order.getShipmentAddress()),
                orderItems,
                CartDto.mapToDto(order.getCart())
        );
    }
    /*

     */
}
