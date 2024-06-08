package com.payment.service;

import com.payment.client.OrderClient;
import com.payment.client.UserClient;
import com.payment.entity.Payment;
import com.payment.model.request.DeductRequest;
import com.payment.model.request.PaymentRequest;
import com.payment.model.response.OrderResponse;
import com.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final UserClient userClient;

    public ResponseEntity<?> makePayment(PaymentRequest paymentRequest, String userEmail) {
        OrderResponse orderResponse = orderClient.getOrderByOrderId(paymentRequest.getOrderId(), userEmail);
        if(orderResponse.isPaid()) return ResponseEntity.internalServerError().body("Order already paid");
        try {
            Long userId = userClient.deductMoneyFromCardAndGetUserId(DeductRequest.
                    builder()
                    .cardNumber(paymentRequest.getCardNumber())
                    .cvv(paymentRequest.getCvv())
                    .cardExpiryMonth(paymentRequest.getCardExpiryMonth())
                    .cardExpiryYear(paymentRequest.getCardExpiryYear())
                    .amount(orderResponse.getTotalPrice())
                    .build(), userEmail);

            Payment payment = Payment.builder().paymentDate(new Date()).userId(userId).orderId(orderResponse.getOrderId()).build();
            orderClient.setOrderAsPaid(paymentRequest.getOrderId());
            paymentRepository.save(payment);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok("Payment done successfully");
    }
}
