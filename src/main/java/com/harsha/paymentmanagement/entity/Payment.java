package com.harsha.paymentmanagement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String initiatedBy;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private BankAccount fromAccount;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private BankAccount toAccount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 30)
    private String paymentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentStatus status;

    @Column(length = 60)
    private String reviewedBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Payment() {}

    public Payment(String initiatedBy, BankAccount fromAccount, BankAccount toAccount,
                   BigDecimal amount, String paymentType, PaymentStatus status) {
        this.initiatedBy = initiatedBy;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.paymentType = paymentType;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getInitiatedBy() { return initiatedBy; }
    public BankAccount getFromAccount() { return fromAccount; }
    public BankAccount getToAccount() { return toAccount; }
    public BigDecimal getAmount() { return amount; }
    public String getPaymentType() { return paymentType; }
    public PaymentStatus getStatus() { return status; }
    public String getReviewedBy() { return reviewedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }
}
