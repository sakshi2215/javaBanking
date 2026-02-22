package com.sakshi.banking.repository;

import com.sakshi.banking.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {

}
