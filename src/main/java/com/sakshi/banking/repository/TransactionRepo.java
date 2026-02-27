package com.sakshi.banking.repository;

import com.sakshi.banking.entity.Account;
import com.sakshi.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccount(Account account);
}
