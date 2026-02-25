package com.sakshi.banking.service;
import com.sakshi.banking.dto.request.CreateAddressRequest;
import com.sakshi.banking.dto.response.AddressResponse;
import com.sakshi.banking.entity.Address;
import com.sakshi.banking.entity.AddressType;
import com.sakshi.banking.entity.Customer;
import com.sakshi.banking.exceptions.ResourceNotFoundException;
import com.sakshi.banking.exceptions.customer.CustomerNotFoundException;
import com.sakshi.banking.repository.CustomerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service responsible for managing customer address operations.
 *
 * <p><strong>Domain Responsibility:</strong>
 * Handles creation and association of {@code Address} entities
 * with their respective {@code Customer}.
 *
 * <p><strong>Persistence Strategy:</strong>
 * Addresses are persisted via cascading from the {@code Customer}
 * entity. Saving the customer automatically saves associated
 * addresses.
 *
 * <p><strong>Transaction Management:</strong>
 * Address creation executes within a single database transaction.
 * If any validation fails, no data is persisted.
 */
@Service
public class AddressService {
    @Autowired
    public CustomerRepo customerRepo;

    /**
     * Creates and associates a new address with an existing customer.
     *
     * <p>This method performs the following steps:
     * <ul>
     *     <li>Retrieves the customer by ID</li>
     *     <li>Validates that the customer exists</li>
     *     <li>Constructs a new {@code Address} entity</li>
     *     <li>Associates the address with the customer</li>
     *     <li>Persists the address via cascade save</li>
     * </ul>
     *
     * <p><strong>Cascade Behavior:</strong>
     * The {@code Address} entity is saved automatically when
     * the {@code Customer} entity is persisted.
     *
     * @param request request containing address details and customer ID
     * @return AddressResponse containing saved address details
     * @throws ResourceNotFoundException if the customer does not exist
     * @throws IllegalArgumentException if the address type is invalid
     */
    @Transactional
    public AddressResponse createAddress(CreateAddressRequest request){

        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Address address = new Address();
        address.setAddressType(AddressType.valueOf(request.getAddressType().toUpperCase()));
        address.setCity(request.getCity());
        address.setPostalCode(request.getPostalCode());
        address.setState(request.getState());
        address.setStreet(request.getStreet());
        address.setPrimary(request.isPrimary());
        address.setCustomer(customer);

        customer.addAddress(address);

        //Save only customer (cascade saves address automatically)
        customerRepo.save(customer);

        return new AddressResponse(
                address.getAddressType(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.isPrimary(),
                customer.getCustomerId()
        );
    }

    //WILL IMPLEMENT TOMORROW

//    public boolean removeAddress(Long customerId){
//        Customer customer = customerRepo.findById(customerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
//
//        customer.removeAddress();
//    }

}
