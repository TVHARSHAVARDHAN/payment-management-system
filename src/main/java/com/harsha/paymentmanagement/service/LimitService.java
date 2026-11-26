package com.harsha.paymentmanagement.service;

import com.harsha.paymentmanagement.dto.LimitRequest;
import com.harsha.paymentmanagement.entity.TransactionLimit;
import com.harsha.paymentmanagement.repository.TransactionLimitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LimitService {
    private final TransactionLimitRepository limits;

    public LimitService(TransactionLimitRepository limits) {
        this.limits = limits;
    }

    @Transactional
    public TransactionLimit create(LimitRequest request) {
        if (request.approvalThreshold().compareTo(request.maxAmount()) > 0) {
            throw new IllegalArgumentException("Approval threshold cannot exceed the maximum transaction limit");
        }
        return limits.save(new TransactionLimit(request.scope(), request.scopeValue(),
                request.maxAmount(), request.approvalThreshold()));
    }

    public List<TransactionLimit> listAll() {
        return limits.findAll();
    }
}
