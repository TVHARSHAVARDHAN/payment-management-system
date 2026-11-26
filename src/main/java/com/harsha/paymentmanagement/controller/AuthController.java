package com.harsha.paymentmanagement.controller;

import com.harsha.paymentmanagement.dto.MessageResponse;
import com.harsha.paymentmanagement.dto.RegisterRequest;
import com.harsha.paymentmanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return new MessageResponse("User registered. Use HTTP Basic authentication for protected endpoints.");
    }
}
