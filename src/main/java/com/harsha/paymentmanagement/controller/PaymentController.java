package com.harsha.paymentmanagement.controller;

import com.harsha.paymentmanagement.dto.PaymentRequest;
import com.harsha.paymentmanagement.entity.Payment;
import com.harsha.paymentmanagement.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment initiate(@Valid @RequestBody PaymentRequest request, Authentication auth) {
        return paymentService.initiate(request, auth.getName());
    }

    @GetMapping
    public List<Payment> mine(Authentication auth) {
        return paymentService.listMine(auth.getName());
    }
}
