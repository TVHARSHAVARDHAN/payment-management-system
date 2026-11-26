package com.harsha.paymentmanagement.repository;

import com.harsha.paymentmanagement.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByOwnerUsername(String ownerUsername);
    Optional<BankAccount> findByIdAndOwnerUsername(Long id, String ownerUsername);
}
