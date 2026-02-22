package com.sakshi.banking.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LoanType {
    HOME, PERSONAL, CAR;
    @JsonCreator
    public static LoanType from(String value) {
        return LoanType.valueOf(value.toUpperCase());
    }
}
