package com.sakshi.banking.controller;
import com.sakshi.banking.dto.request.CreateCustomerRequest;
import com.sakshi.banking.dto.response.CustomerResponse;
import com.sakshi.banking.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class CustomerController {

    private final CustomerService customerService;

    public  CustomerController (CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping("create")
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody  CreateCustomerRequest request){
        CustomerResponse response = customerService.createCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
