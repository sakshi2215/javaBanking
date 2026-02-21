package com.sakshi.banking.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {

    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private String status;
    private LocalDate openedDate;
}

/*
* accountNumber
accountType
balance
status
openedDate
* */