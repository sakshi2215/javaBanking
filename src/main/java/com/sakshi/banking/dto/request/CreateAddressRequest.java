package com.sakshi.banking.dto.request;
import com.sakshi.banking.entity.AddressType;
import com.sakshi.banking.validation.annotation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/*
* street ->string
* city -> string
* state ->string
* postalcode ->string
* addresstype ->enum
* isPrimary ->boolean
* */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAddressRequest {

    @NotBlank(message = "Street detail is mandatory")
    @Size(min=5, max = 100, message = "Street detail must be between 5 and 100 character")
    private String street;

    @NotBlank(message = "City cannot be blank")
    @Size(min = 5, max = 50, message = "City name must be between 5 and 50 character")
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Size(min = 5, max = 50, message = "State name must be between 5 and 50 character")
    private String state;

    @NotBlank(message = "Postal code cannot be left blank")
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "Please enter valid Postal Code"
    )
    private String postalCode;

    @NotNull(message = "Please choose correct addressType")
    @ValidEnum(enumClass = AddressType.class, message = "Address type must be HOME,OFFICE,BILLING or CORRESPONDENCE")
    private String addressType;

    @NotNull(message = "Primary flag must be provided")
    private boolean isPrimary;


}
