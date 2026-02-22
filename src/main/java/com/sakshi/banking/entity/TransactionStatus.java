package com.sakshi.banking.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransactionStatus {
    PENDING, SUCCESS, FAILED, REVERSED;
    @JsonCreator
    public static TransactionStatus from(String value) {
        return TransactionStatus.valueOf(value.toUpperCase());
    }
}
