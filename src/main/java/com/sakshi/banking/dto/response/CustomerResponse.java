package com.sakshi.banking.dto.response;


import com.sakshi.banking.entity.Gender;
import com.sakshi.banking.entity.KycStatus;
import com.sakshi.banking.entity.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private Long customerId;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNo;
    private Gender gender;
    private Status customerStatus;
    private KycStatus kycStatus;

}

/*
* customerId
firstName
lastName
email
phone
dateOfBirth
gender
customerStatus
kycStatus
*/