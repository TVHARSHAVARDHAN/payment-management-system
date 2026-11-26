
package com.harsha.paymentmanagement.controller;

import com.harsha.paymentmanagement.dto.LimitRequest;
import com.harsha.paymentmanagement.entity.TransactionLimit;
import com.harsha.paymentmanagement.entity.Payment;
import com.harsha.paymentmanagement.service.LimitService;
import com.harsha.paymentmanagement.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final LimitService limitService;
    private final PaymentService paymentService;

    public AdminController(LimitService limitService, PaymentService paymentService) {
        this.limitService = limitService;
        this.paymentService = paymentService;
    }

    @PostMapping("/limits")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionLimit createLimit(@Valid @RequestBody LimitRequest request) {
        return limitService.create(request);
    }

    @GetMapping("/limits")
    public List<TransactionLimit> listLimits() {
        return limitService.listAll();
    }

    @GetMapping("/payments")
    public List<Payment> listAllPayments() {
        return paymentService.listAll();
    }

    @PostMapping("/payments/{id}/approve")
    public Object approve(@PathVariable Long id, Authentication auth) {
        return paymentService.approve(id, auth.getName());
    }

    @PostMapping("/payments/{id}/reject")
    public Object reject(@PathVariable Long id, Authentication auth) {
        return paymentService.reject(id, auth.getName());
    }
}
