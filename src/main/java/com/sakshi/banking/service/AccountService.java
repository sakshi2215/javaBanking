package com.sakshi.banking.service;

import com.sakshi.banking.dto.request.CreateAccountRequest;
import com.sakshi.banking.dto.response.AccountResponse;
import com.sakshi.banking.entity.Account;
import com.sakshi.banking.entity.AccountType;
import com.sakshi.banking.entity.Customer;
import com.sakshi.banking.exceptions.account.AccountNotFoundException;
import com.sakshi.banking.exceptions.customer.CustomerNotFoundException;
import com.sakshi.banking.repository.AccountRepo;
import com.sakshi.banking.repository.CustomerRepo;
import com.sakshi.banking.utility.AccountNumberGenerationUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for managing account lifecycle operations.
 *
 * <p>
 * <strong>Domain Responsibility:</strong>
 * Handles account creation and ensures proper association
 * between {@code Customer} and {@code Account} entities.
 *
 * <p>
 * <strong>Business Rules:</strong>
 * <ul>
 * <li>An account can only be created for an existing customer</li>
 * <li>The customer must have at least one registered address</li>
 * <li>A unique account number is generated during creation</li>
 * </ul>
 *
 * <p>
 * <strong>Transaction Management:</strong>
 * Account creation executes within a single database transaction.
 * If any validation fails, no account is persisted.
 */
@Service
public class AccountService {

    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;

    public AccountService(CustomerRepo customerRepo, AccountRepo accountRepo) {

        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
    }

    /**
     * Creates a new bank account for an existing customer.
     *
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Retrieves the customer by ID</li>
     * <li>Validates that the customer exists</li>
     * <li>Ensures the customer has at least one registered address</li>
     * <li>Generates a unique account number</li>
     * <li>Persists the newly created account</li>
     * </ul>
     *
     * <p>
     * <strong>Initialization Behavior:</strong>
     * The account is created with default balance and status
     * as defined in the {@code Account} entity configuration.
     *
     * @param request    request containing account type information
     * @param customerId unique identifier of the customer
     * @return AccountResponse containing account details
     * @throws CustomerNotFoundException if the customer does not exist
     * @throws IllegalStateException     if the customer does not have
     *                                   a registered address
     */
    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request, Long customerId) {

        // Fetch customer using customer Repo and verify if it exists
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer does not exist with the given ID"));

        // Verify if customer has at least one address
        if (customer.getAddressList() == null)
            throw new IllegalStateException("Customer does not have any address, Please provide to proceed further");

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
                savedResponse.getOpenedDate());

    }

    /**
     * Gets bank account details by account Number.
     *
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Retrieves the account details by account number</li>
     * </ul>
     *
     * @param accountNumber unique identifier of the account
     * @return AccountResponse containing account details
     * @throws AccountNotFoundException if the customer does not exist
     */
    public AccountResponse getAccountDetails(String accountNumber) {
        Account account = accountRepo.findByAccountNumber(accountNumber).orElseThrow(
                () -> new AccountNotFoundException("Account does not exists"));

        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType().name())
                .status(account.getStatus().name())
                .balance(account.getBalance())
                .openedDate(account.getOpenedDate())
                .build();

    }

    public List<AccountResponse> getAccountsbyCustomer(Long customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer does not exist with the given ID"));

        List<Account> customerAccounts = accountRepo.findByCustomer(customer);

        return customerAccounts.stream().map(account -> AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType().name())
                .status(account.getStatus().name())
                .balance(account.getBalance())
                .openedDate(account.getOpenedDate())
                .build()).collect(Collectors.toList());
    }

}
