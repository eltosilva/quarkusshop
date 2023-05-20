package com.targa.labs.services;

import com.targa.labs.models.Order;
import com.targa.labs.models.OrderStatus;
import com.targa.labs.models.Payment;
import com.targa.labs.models.PaymentStatus;
import com.targa.labs.repositories.OrderRepository;
import com.targa.labs.repositories.PaymentRepository;
import com.targa.labs.services.dtos.PaymentDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
@AllArgsConstructor
public class PaymentService {

    private PaymentRepository paymentRepository;
    private OrderRepository orderRepository;

    public List<PaymentDto> findByPriceRange(Double max) {
        return paymentRepository.findAllByAmountBetween(BigDecimal.ZERO, BigDecimal.valueOf(max))
                .stream()
                .map(payment -> mapToDto(payment, findOrderByPaymentId(payment.getId()).getId()))
                .collect(Collectors.toList());
    }

    public List<PaymentDto> findAll() {
        return paymentRepository.findAll()
                .stream()
                .map(payment -> {
                    Order order = orderRepository.findByPaymentId(payment.getId())
                            .orElseThrow(() -> new IllegalStateException("The Order does not exist!"));
                    return mapToDto(payment, order.getId());
                })
                .collect(Collectors.toList());
    }

    public PaymentDto findById(Long id) {
        log.debug("Request to get Payment : {}", id);
        Order order = orderRepository.findByPaymentId(id)
                .orElseThrow(() -> new IllegalStateException("The Order does not exist!"));

        return paymentRepository.findById(id)
                .map(payment -> mapToDto(payment, order.getId()))
                .orElse(null);
    }

    public PaymentDto create(PaymentDto paymentDto) {
        log.debug("Request to create Payment : {}", paymentDto);

        Order order = orderRepository.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new IllegalStateException("The Order does not exists!"));
        order.setStatus(OrderStatus.PAID);

        Payment payment = paymentRepository.saveAndFlush(
                new Payment(paymentDto.getPaypalPaymentId(),
                        PaymentStatus.valueOf(paymentDto.getStatus()),
                        order.getPrice()
                        ));
        orderRepository.saveAndFlush(order);

        return mapToDto(payment, order.getId());
    }

    public Order findOrderByPaymentId(Long id) {
        return orderRepository.findByPaymentId(id)
                .orElseThrow(() -> new IllegalStateException("No order exists for the Payment ID " + id));
    }

    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }

    public static PaymentDto mapToDto(Payment payment, Long orderId){
        if(payment == null)
            return null;
        return new PaymentDto(payment.getId(), payment.getPaypalPaymentId(), payment.getStatus().name(), orderId);
    }
}
