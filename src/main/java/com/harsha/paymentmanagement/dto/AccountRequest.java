package com.harsha.paymentmanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record AccountRequest(
        @NotNull @Pattern(regexp = "[A-Za-z0-9-]{6,30}") String accountNumber,
        @NotNull @DecimalMin("0.00") BigDecimal openingBalance
) {}
