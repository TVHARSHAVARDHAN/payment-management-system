package com.harsha.paymentmanagement.dto;

import com.harsha.paymentmanagement.entity.LimitScope;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record LimitRequest(
        @NotNull LimitScope scope,
        @NotBlank String scopeValue,
        @NotNull @DecimalMin("0.01") BigDecimal maxAmount,
        @NotNull @DecimalMin("0.01") BigDecimal approvalThreshold
) {}
