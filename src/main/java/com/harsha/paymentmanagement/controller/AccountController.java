package com.harsha.paymentmanagement.controller;

import com.harsha.paymentmanagement.dto.AccountRequest;
import com.harsha.paymentmanagement.entity.BankAccount;
import com.harsha.paymentmanagement.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) { this.accountService = accountService; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BankAccount create(@Valid @RequestBody AccountRequest request, Authentication auth) {
        return accountService.create(request, auth.getName());
    }

    @GetMapping
    public List<BankAccount> mine(Authentication auth) {
        return accountService.listMine(auth.getName());
    }
}
