package com.harsha.paymentmanagement.service;

import com.harsha.paymentmanagement.dto.AccountRequest;
import com.harsha.paymentmanagement.entity.BankAccount;
import com.harsha.paymentmanagement.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AccountService {
    private final BankAccountRepository accounts;

    public AccountService(BankAccountRepository accounts) {
        this.accounts = accounts;
    }

    @Transactional
    public BankAccount create(AccountRequest request, String username) {
        if (accounts.findAll().stream().anyMatch(a -> a.getAccountNumber().equals(request.accountNumber()))) {
            throw new IllegalStateException("Account number already exists");
        }
        return accounts.save(new BankAccount(request.accountNumber(), username, request.openingBalance()));
    }

    public List<BankAccount> listMine(String username) {
        return accounts.findByOwnerUsername(username);
    }
}
