package com.payment.controller;

import com.payment.model.request.PaymentRequest;
import com.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentApi {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> makePayment(@RequestBody PaymentRequest paymentRequest, @RequestHeader String userEmail) {
        return paymentService.makePayment(paymentRequest,userEmail);
    }
}
