package com.sakshi.banking.dto.request;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {
    @NotNull(message = "Account Number cannot be blank")
    private String fromAccountNumber;

    @NotNull(message = "Account Number cannot be blank")
    private String toAccountNumber;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=5, fraction=2)
    private BigDecimal amount;

}
