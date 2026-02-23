package com.sakshi.banking.service;
import com.sakshi.banking.dto.request.CreateCustomerRequest;
import com.sakshi.banking.dto.response.CustomerResponse;
import com.sakshi.banking.entity.Customer;
import com.sakshi.banking.entity.Gender;
import com.sakshi.banking.entity.Status;
import com.sakshi.banking.exceptions.ResourceAlreadyExistsException;
import com.sakshi.banking.exceptions.ResourceNotFoundException;
import com.sakshi.banking.repository.CustomerRepo;
import com.sakshi.banking.utility.PhoneNumberUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {


    private final CustomerRepo customerRepository;

    public CustomerService(CustomerRepo customerRepository){
        this.customerRepository = customerRepository;
    }


    /**
     * Create new Customer
     * @param request (CustomerRequest)
     * @return CustomerResponse if Customer is created
     */
    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request){

        //check if customer already exists or not
        if(customerRepository.existsByEmail(request.getEmail()) || customerRepository.existsByPhone(request.getPhone())){
            throw new ResourceAlreadyExistsException("Customer Already Exists with the email or phone Number");
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
                savedCustomer.getKycStatus()
        );
    }

    /**
     * Get Customer Details
     * @param customerId
     * @return CustomerResponse if Customer exists
     */
    public CustomerResponse findCustomerById(Long customerId){
        Customer savedCustomer = customerRepository.findById(customerId).orElseThrow(()->
                new ResourceNotFoundException("Customer does not exists with this ID"));

        //-- HAVE TO MODIFY-- Exception to throw
        if(savedCustomer.getCustomerStatus()== Status.CLOSED){
            throw  new ResourceNotFoundException("Customer is Inactive");
        }

        return new CustomerResponse(
                savedCustomer.getCustomerId(),
                savedCustomer.getLastName(),
                savedCustomer.getFirstName(),
                savedCustomer.getEmail(),
                savedCustomer.getPhone(),
                savedCustomer.getGender(),
                savedCustomer.getCustomerStatus(),
                savedCustomer.getKycStatus()
        );
    }

    /**
     * Delete customer by ID
     * @param customerId
     * @return true if deleted else false
     */
    @Transactional
    public boolean deleteCustomer(Long customerId){
        Customer savedCustomer = customerRepository.findById(customerId).orElseThrow(()->
                new ResourceNotFoundException("Customer does not exists with this ID"));
        savedCustomer.setCustomerStatus(Status.CLOSED);
        Customer modifiedCustomer = customerRepository.save(savedCustomer);
        if(modifiedCustomer.getCustomerStatus()==Status.CLOSED) return true;
        return false;

    }

}
