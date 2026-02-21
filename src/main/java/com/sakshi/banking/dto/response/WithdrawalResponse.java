package com.sakshi.banking.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawalResponse {
    private String transactionId;
    private String referenceNumber;
    private String transactionType;
    private BigDecimal amount;
    private String transactionStatus;
    private LocalDateTime transactionDate;
    private String accountNumber;
    private String balanceAfterTransaction;
    private String description;
}
