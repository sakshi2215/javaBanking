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


    public CustomerResponse findCustomerById(Long customerId){
        Customer savedCustomer = customerRepository.findById(customerId).orElseThrow(()->
                new ResourceNotFoundException("Customer does not exists with this ID"));

        //-- HAVE TO MODIFY
        if(savedCustomer.getCustomerStatus()== Status.CLOSED){
            throw  new RuntimeException("Customer does not exits");
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

    @Transactional
    public void deleteCustomer(Long customerId){
        Customer savedCustomer = customerRepository.findById(customerId).orElseThrow(()->
                new ResourceNotFoundException("Customer does not exixts with this ID"));
        savedCustomer.setCustomerStatus(Status.CLOSED);
        customerRepository.save(savedCustomer);

    }

}
