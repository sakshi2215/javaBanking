package com.sakshi.banking.dto.request;

/*
* CreateCustomerRequest

firstName
lastName
email
phone
dateOfBirth
gender
*
* Customer
 ├── Account
 │    ├── Card
 │    ├── Transaction
 │    ├── Cheque
 │
 ├── Address
 ├── Beneficiary
 ├── Loan
 ├── Notification
 ├── Online Banking
* */

import com.sakshi.banking.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.processing.Pattern;
import java.time.LocalDate;


public class CreateCustomerRequest {

    @NotBlank(message = "First Name is required")
    @Size(min = 2, max = 50, message = "First Name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "Name must contain only alphabets and spaces"
    )
    private String firstName;


    @Size(min = 2, max = 50, message = "Last Name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "Name must contain only alphabets and spaces"
    )
    private String lastName;

    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    private LocalDate dateOfBirth;

    private Gender gender;

}
