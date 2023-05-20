package com.targa.labs.services.dtos;

import com.targa.labs.models.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long id;
    private Long quantity;
    private Long productId;
    private Long orderId;

    public static OrderItemDto mapToDto(OrderItem orderItem){
        return new OrderItemDto(orderItem.getId(), orderItem.getQuantity(), orderItem.getProduct().getId(),
                orderItem.getOrder().getId());
    }
}
