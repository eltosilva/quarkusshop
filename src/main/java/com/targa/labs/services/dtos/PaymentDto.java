package com.targa.labs.services.dtos;

import com.targa.labs.models.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private String paypalPaymentId;
    private String status;
    private Long orderId;

    public static PaymentDto mapToDto(Payment payment, Long orderId){
        if(payment == null)
            return null;
        return new PaymentDto(payment.getId(), payment.getPaypalPaymentId(), payment.getStatus().name(), orderId);
    }
}
