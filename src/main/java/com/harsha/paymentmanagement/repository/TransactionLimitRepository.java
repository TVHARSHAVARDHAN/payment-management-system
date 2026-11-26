package com.harsha.paymentmanagement.repository;

import com.harsha.paymentmanagement.entity.TransactionLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionLimitRepository extends JpaRepository<TransactionLimit, Long> {
    List<TransactionLimit> findByActiveTrue();
}
