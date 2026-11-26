package com.harsha.paymentmanagement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transaction_limits")
public class TransactionLimit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LimitScope scope;

    // USER: username, ACCOUNT: account ID as text, TYPE: payment type such as TRANSFER
    @Column(nullable = false, length = 100)
    private String scopeValue;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal maxAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal approvalThreshold;

    @Column(nullable = false)
    private boolean active = true;

    protected TransactionLimit() {}

    public TransactionLimit(LimitScope scope, String scopeValue,
                            BigDecimal maxAmount, BigDecimal approvalThreshold) {
        this.scope = scope;
        this.scopeValue = scopeValue;
        this.maxAmount = maxAmount;
        this.approvalThreshold = approvalThreshold;
    }

    public Long getId() { return id; }
    public LimitScope getScope() { return scope; }
    public String getScopeValue() { return scopeValue; }
    public BigDecimal getMaxAmount() { return maxAmount; }
    public BigDecimal getApprovalThreshold() { return approvalThreshold; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
