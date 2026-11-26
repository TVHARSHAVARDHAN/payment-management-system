package com.harsha.paymentmanagement.service;

import com.harsha.paymentmanagement.dto.PaymentRequest;
import com.harsha.paymentmanagement.entity.*;
import com.harsha.paymentmanagement.repository.BankAccountRepository;
import com.harsha.paymentmanagement.repository.PaymentRepository;
import com.harsha.paymentmanagement.repository.TransactionLimitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {
    private final BankAccountRepository accounts;
    private final PaymentRepository payments;
    private final TransactionLimitRepository limits;

    public PaymentService(BankAccountRepository accounts, PaymentRepository payments,
                          TransactionLimitRepository limits) {
        this.accounts = accounts;
        this.payments = payments;
        this.limits = limits;
    }

    @Transactional
    public Payment initiate(PaymentRequest request, String username) {
        if (request.fromAccountId().equals(request.toAccountId())) {
            throw new IllegalArgumentException("Source and destination accounts must be different");
        }
        BankAccount from = accounts.findByIdAndOwnerUsername(request.fromAccountId(), username)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found for current user"));
        BankAccount to = accounts.findById(request.toAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        if (!from.isActive() || !to.isActive()) {
            throw new IllegalStateException("Both accounts must be active");
        }
        if (from.getBalance().compareTo(request.amount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        boolean approvalRequired = false;
        for (TransactionLimit rule : limits.findByActiveTrue()) {
            if (matches(rule, username, from.getId(), request.paymentType())) {
                if (request.amount().compareTo(rule.getMaxAmount()) > 0) {
                    throw new IllegalArgumentException("Payment exceeds the configured " + rule.getScope() + " limit");
                }
                if (request.amount().compareTo(rule.getApprovalThreshold()) >= 0) {
                    approvalRequired = true;
                }
            }
        }

        PaymentStatus status = approvalRequired ? PaymentStatus.PENDING_APPROVAL : PaymentStatus.COMPLETED;
        Payment payment = payments.save(new Payment(username, from, to, request.amount(),
                request.paymentType().trim().toUpperCase(), status));

        if (status == PaymentStatus.COMPLETED) {
            transfer(from, to, request.amount());
        }
        return payment;
    }

    private boolean matches(TransactionLimit rule, String username, Long accountId, String type) {
        return switch (rule.getScope()) {
            case USER -> rule.getScopeValue().equalsIgnoreCase(username);
            case ACCOUNT -> rule.getScopeValue().equals(accountId.toString());
            case TYPE -> rule.getScopeValue().equalsIgnoreCase(type.trim());
        };
    }

    private void transfer(BankAccount from, BankAccount to, BigDecimal amount) {
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        accounts.save(from);
        accounts.save(to);
    }

    @Transactional
    public Payment approve(Long paymentId, String adminUsername) {
        Payment payment = payments.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (payment.getStatus() != PaymentStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Only pending payments can be approved");
        }
        BankAccount from = payment.getFromAccount();
        BankAccount to = payment.getToAccount();
        if (from.getBalance().compareTo(payment.getAmount()) < 0) {
            throw new IllegalStateException("Source account no longer has sufficient balance");
        }
        transfer(from, to, payment.getAmount());
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setReviewedBy(adminUsername);
        return payments.save(payment);
    }

    @Transactional
    public Payment reject(Long paymentId, String adminUsername) {
        Payment payment = payments.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (payment.getStatus() != PaymentStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Only pending payments can be rejected");
        }
        payment.setStatus(PaymentStatus.REJECTED);
        payment.setReviewedBy(adminUsername);
        return payments.save(payment);
    }

    public List<Payment> listMine(String username) {
        return payments.findByInitiatedByOrderByCreatedAtDesc(username);
    }
    public List<Payment> listAll() {
        return payments.findAll(
            org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.DESC,
                "createdAt"
            )
        );
    }
}
