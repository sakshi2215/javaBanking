package com.sakshi.banking.service;
import com.sakshi.banking.dto.request.CreateAddressRequest;
import com.sakshi.banking.dto.response.AddressResponse;
import com.sakshi.banking.entity.Address;
import com.sakshi.banking.entity.AddressType;
import com.sakshi.banking.entity.Customer;
import com.sakshi.banking.exceptions.ResourceNotFoundException;
import com.sakshi.banking.repository.CustomerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    public CustomerRepo customerRepo;

    @Transactional
    public AddressResponse createAddress(CreateAddressRequest request){

        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

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
