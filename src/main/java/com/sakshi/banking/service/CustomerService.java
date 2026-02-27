package com.sakshi.banking.service;

import com.sakshi.banking.dto.request.CreateCustomerRequest;
import com.sakshi.banking.dto.response.CustomerResponse;
import com.sakshi.banking.entity.Customer;
import com.sakshi.banking.entity.Gender;
import com.sakshi.banking.entity.Status;
import com.sakshi.banking.exceptions.customer.CustomerAlreadyExistsException;
import com.sakshi.banking.exceptions.customer.CustomerInactiveException;
import com.sakshi.banking.exceptions.customer.CustomerNotFoundException;
import com.sakshi.banking.repository.CustomerRepo;
import com.sakshi.banking.utility.PhoneNumberUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * Service responsible for managing customer lifecycle operations.
 *
 * <p>
 * <strong>Domain Responsibility:</strong>
 * Handles creation, retrieval, and soft deletion of {@code Customer} entities.
 *
 * <p>
 * <strong>Business Rules:</strong>
 * <ul>
 * <li>A customer cannot be created if an account with the same email or phone
 * already exists</li>
 * <li>Deleting a customer marks the customer status as {@code CLOSED} instead
 * of physical deletion</li>
 * <li>Inactive or closed customers cannot be retrieved for normal
 * operations</li>
 * </ul>
 *
 * <p>
 * <strong>Transaction Management:</strong>
 * Customer creation and deletion execute within a single database transaction.
 * Retrieval is read-only and does not require a transaction.
 */
@Service
public class CustomerService {

    private final CustomerRepo customerRepository;

    public CustomerService(CustomerRepo customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Creates a new customer in the system.
     *
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Checks if a customer with the same email or phone already exists</li>
     * <li>Formats the phone number according to Indian format</li>
     * <li>Creates a new {@code Customer} entity</li>
     * <li>Persists the customer in the database</li>
     * </ul>
     *
     * @param request contains customer details such as first name, last name,
     *                email, phone, date of birth, and gender
     * @return CustomerResponse containing customer details after creation
     * @throws CustomerAlreadyExistsException if a customer with the same email or
     *                                        phone already exists
     */
    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {

        // check if customer already exists or not
        if (customerRepository.existsByEmail(request.getEmail())
                || customerRepository.existsByPhone(request.getPhone())) {
            throw new CustomerAlreadyExistsException("Customer Already Exists with the email or phone Number");
        }

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        String formattedPhone = PhoneNumberUtil.formatIndianNumber(request.getPhone());
        customer.setPhone(formattedPhone);
        customer.setEmail(request.getEmail());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(Gender.valueOf(request.getGender().toUpperCase()));

        Customer savedCustomer = customerRepository.save(customer);

        return new CustomerResponse(
                savedCustomer.getCustomerId(),
                savedCustomer.getLastName(),
                savedCustomer.getFirstName(),
                savedCustomer.getEmail(),
                savedCustomer.getPhone(),
                savedCustomer.getGender(),
                savedCustomer.getCustomerStatus(),
                savedCustomer.getKycStatus());
    }

    /**
     * Retrieves customer details by ID.
     *
     * <p>
     * This method performs the following:
     * <ul>
     * <li>Fetches the {@code Customer} entity by ID</li>
     * <li>Throws an exception if the customer does not exist</li>
     * <li>Prevents access if the customer is inactive or closed</li>
     * </ul>
     *
     * @param customerId unique identifier of the customer
     * @return CustomerResponse containing customer details
     * @throws CustomerNotFoundException if the customer does not exist or is
     *                                   inactive/closed
     */
    @Transactional
    public CustomerResponse findCustomerById(Long customerId) {
        Customer savedCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer does not exists with this ID"));

        if (savedCustomer.getCustomerStatus() == Status.CLOSED) {
            throw new CustomerInactiveException("Customer is Inactive");
        }

        return new CustomerResponse(
                savedCustomer.getCustomerId(),
                savedCustomer.getLastName(),
                savedCustomer.getFirstName(),
                savedCustomer.getEmail(),
                savedCustomer.getPhone(),
                savedCustomer.getGender(),
                savedCustomer.getCustomerStatus(),
                savedCustomer.getKycStatus());
    }

    /**
     * Soft deletes a customer by marking their status as {@code CLOSED}.
     *
     * <p>
     * This method performs the following:
     * <ul>
     * <li>Fetches the {@code Customer} entity by ID</li>
     * <li>Throws an exception if the customer does not exist</li>
     * <li>Updates the customer status to {@code CLOSED}</li>
     * <li>Persists the updated status in the database</li>
     * </ul>
     *
     * @param customerId unique identifier of the customer to delete
     * @return true if the customer status was successfully updated to
     *         {@code CLOSED}, else false
     * @throws CustomerNotFoundException if the customer does not exist
     */
    @Transactional
    public void deleteCustomer(Long customerId) {
        Customer savedCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer does not exists with this ID"));
        savedCustomer.setCustomerStatus(Status.CLOSED);
        Customer modifiedCustomer = customerRepository.save(savedCustomer);

    }

    /**
     * Gets all bank accounts for a customer.
     * 
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Retrieves the customer by ID</li>
     * <li>Validates that the customer exists</li>
     * <li>Retrieves all accounts for the customer</li>
     * </ul>
     * 
     * @param customerId unique identifier of the customer
     * @return List of AccountResponse containing account details
     * @throws CustomerNotFoundException if the customer does not exist
     */
    public List<AccountResponse> getAccountsbyCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer does not exists with this ID"));

        List<Account> accounts = accountRepository.findByCustomer(customer);

        return accounts.stream().map(account -> AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType().name())
                .balance(account.getBalance().toString())
                .status(account.getStatus().name())
                .openedDate(account.getOpenedDate())
                .build()).collect(Collectors.toList());
    }

    /**
     * Gets customer details by account number.
     * 
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Retrieves the account by account number</li>
     * <li>Validates that the account exists</li>
     * <li>Retrieves the customer associated with the account</li>
     * </ul>
     * 
     * @param accountNumber unique identifier of the account
     * @return CustomerResponse containing customer details
     * @throws AccountNotFoundException if the account does not exist
     */
    public CustomerResponse getCustomerByAccountNumber(String accountNumber) {
        Account account = accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account with provided number does not exists"));

        Customer customer = account.getCustomer();

        return new CustomerResponse(
                customer.getCustomerId(),
                customer.getLastName(),
                customer.getFirstName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getGender(),
                customer.getCustomerStatus(),
                customer.getKycStatus());
    }

    /**
     * Updates customer details.
     * 
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Retrieves the customer by ID</li>
     * <li>Validates that the customer exists</li>
     * <li>Updates the customer details</li>
     * </ul>
     * 
     * @param customerId unique identifier of the customer
     * @param request    contains customer details to update
     * @return CustomerResponse containing updated customer details
     * @throws CustomerNotFoundException if the customer does not exist
     */
    public CustomerResponse updateCustomer(Long customerId, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer does not exists with this ID"));

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(Gender.valueOf(request.getGender().toUpperCase()));

        Customer savedCustomer = customerRepository.save(customer);

        return new CustomerResponse(
                savedCustomer.getCustomerId(),
                savedCustomer.getLastName(),
                savedCustomer.getFirstName(),
                savedCustomer.getEmail(),
                savedCustomer.getPhone(),
                savedCustomer.getGender(),
                savedCustomer.getCustomerStatus(),
                savedCustomer.getKycStatus());
    }

}
