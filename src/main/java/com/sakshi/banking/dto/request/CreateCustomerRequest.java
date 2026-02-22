package com.sakshi.banking.dto.request;
import com.sakshi.banking.entity.Gender;
import com.sakshi.banking.validation.annotation.ValidEnum;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(\\+91[\\-\\s]?)?[6-9]\\d{9}$",
            message = "Please enter valid phone number"
    )
    private String phone;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is mandatory")
    @ValidEnum(enumClass = Gender.class, message = "Gender can be MALE, FEMALE OR OTHER")
    private String gender;

}

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
