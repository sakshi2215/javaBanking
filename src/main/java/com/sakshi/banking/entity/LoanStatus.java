package com.sakshi.banking.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LoanStatus {
    PENDING, APPROVED, REJECTED, CLOSED;
    @JsonCreator
    public static LoanStatus from(String value) {
        return LoanStatus.valueOf(value.toUpperCase());
    }
}
