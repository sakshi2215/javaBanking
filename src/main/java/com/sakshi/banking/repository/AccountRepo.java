package com.sakshi.banking.repository;
import com.sakshi.banking.entity.Account;
import com.sakshi.banking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<List<Account>>findByCustomerId(Long customerId);

    List<Account> findByCustomer(Customer customer);
}
