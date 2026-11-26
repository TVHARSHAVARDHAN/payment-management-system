package com.harsha.paymentmanagement.repository;

import com.harsha.paymentmanagement.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByInitiatedByOrderByCreatedAtDesc(String initiatedBy);
}
