package com.sakshi.banking.service;

import com.sakshi.banking.dto.request.CreateAccountRequest;
import com.sakshi.banking.dto.response.AccountResponse;
import com.sakshi.banking.entity.Account;
import com.sakshi.banking.entity.AccountType;
import com.sakshi.banking.entity.Customer;
import com.sakshi.banking.exceptions.ResourceNotFoundException;
import com.sakshi.banking.repository.AccountRepo;
import com.sakshi.banking.repository.CustomerRepo;
import com.sakshi.banking.utility.AccountNumberGenerationUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class AccountService {

    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;

    public AccountService(CustomerRepo customerRepo, AccountRepo accountRepo){

        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request, Long customerId){

        //Fetch customer using customer Repo and verify if it exists
        Customer customer = customerRepo.findById(customerId).orElseThrow(()->
                new ResourceNotFoundException("Customer does not exist with the given ID"));


        // Verify if customer has at least one address
        if(customer.getAddressList()==null) throw new RuntimeException("Customer does not have any address, Please provide to proceed further");

        Account newAccount = new Account();
        newAccount.setAccountType(AccountType.valueOf(request.getAccountType()));
        AccountNumberGenerationUtil accountNumberGeneration = new AccountNumberGenerationUtil();
        newAccount.setAccountNumber(accountNumberGeneration.generateAccountNumber());
        newAccount.setCustomer(customer);

        Account savedResponse = accountRepo.save(newAccount);

        return new AccountResponse(
                savedResponse.getAccountNumber(),
                savedResponse.getAccountType().name(),
                savedResponse.getBalance(),
                savedResponse.getStatus().name(),
                savedResponse.getOpenedDate()
        );


    }

    //-- IMPLEMENT DELETE ACCOUNT FOR FUTURE
    //public boolean deleteAccount()



}
