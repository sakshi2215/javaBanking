package com.sakshi.banking.dto.response;
import com.sakshi.banking.entity.AddressType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {

    private AddressType addressType;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private boolean isPrimary;
    private Long customerId;
}
