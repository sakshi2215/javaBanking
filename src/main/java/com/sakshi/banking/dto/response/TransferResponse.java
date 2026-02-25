package com.sakshi.banking.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferResponse {

    private BigDecimal amount;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal sourceBalanceAfterTransaction;
    private BigDecimal destinationBalanceAfterTransaction;
    private String debitTransactionReference;
    private String creditTransactionReference;
}


