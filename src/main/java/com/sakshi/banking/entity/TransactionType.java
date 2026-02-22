package com.sakshi.banking.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransactionType {
    DEPOSIT, TRANSFER, WITHDRAWAL, INTEREST, FEE;
    @JsonCreator
    public static TransactionType from(String value) {
        return TransactionType.valueOf(value.toUpperCase());
    }
}
