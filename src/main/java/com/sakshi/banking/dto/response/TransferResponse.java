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
    private String transactionId;
    private String referenceNumber;
    private String transactionType;
    private BigDecimal amount;
    private String transactionStatus;
    private LocalDateTime transactionDate;
    private String toAccountNumber;
    private String fromAccountNumber;
    private String balanceAfterTransaction;
    private String description;
}


/*
* transactionId
referenceNumber
transactionType
amount
transactionStatus
transactionDate
accountNumber-> to and from
balanceAfterTransaction
description*/