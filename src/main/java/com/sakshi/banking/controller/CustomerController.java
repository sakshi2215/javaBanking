package com.sakshi.banking.controller;
import com.sakshi.banking.dto.request.CreateAccountRequest;
import com.sakshi.banking.dto.request.CreateAddressRequest;
import com.sakshi.banking.dto.request.CreateCustomerRequest;
import com.sakshi.banking.dto.response.AccountResponse;
import com.sakshi.banking.dto.response.AddressResponse;
import com.sakshi.banking.dto.response.CustomerResponse;
import com.sakshi.banking.service.AccountService;
import com.sakshi.banking.service.AddressService;
import com.sakshi.banking.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class CustomerController {

    private final CustomerService customerService;
    private final AddressService addressService;
    private final AccountService accountService;

    public  CustomerController (CustomerService customerService, AddressService addressService, AccountService accountService){

        this.customerService = customerService;
        this.addressService = addressService;
        this.accountService = accountService;
    }

    @PostMapping("create")
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody  CreateCustomerRequest request){
        CustomerResponse response = customerService.createCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("createAddress")
    public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody CreateAddressRequest request){
        AddressResponse response = addressService.createAddress(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/getCustomer/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long customerId){
        CustomerResponse response = customerService.findCustomerById(customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable Long customerId){
       customerService.deleteCustomer(customerId);

    }

    @PostMapping("/createAccount/{customerId}")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request, @PathVariable Long customerId){
        AccountResponse response = accountService.createAccount(request, customerId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




}
