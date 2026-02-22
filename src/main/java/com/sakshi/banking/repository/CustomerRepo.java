package com.sakshi.banking.repository;

import com.sakshi.banking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long>{

    /**
     * Find a customer by email.
     *
     * @param email user email
     * @return Optional containing the user if found
     */
    Optional <Customer> findByEmail(String email);

    /**
     * Find a customer by email.
     *
     * @param phone user phone
     * @return Optional containing the user if found
     */
    Optional <Customer> findByPhone(String phone);

    /**
     * Check if a customer exists by email.
     *
     * @param email user email
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Check if a customer exists by email.
     *
     * @param phone user phone
     * @return true if user exists, false otherwise
     */
    boolean existsByPhone(String phone);

    /**
     * Find a customer by email.
     *
     * @param customerId user phone
     * @return Optional containing the user if found
     */


}
