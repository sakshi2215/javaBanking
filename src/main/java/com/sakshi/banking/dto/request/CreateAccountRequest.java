package com.sakshi.banking.dto.request;
import com.sakshi.banking.entity.AccountType;
import com.sakshi.banking.validation.annotation.ValidEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountRequest {
    @NotNull(message = "Please provide accountType")
    @ValidEnum(enumClass = AccountType.class, message = "Account type must be savings , current or fixed")
    private String accountType;
}




