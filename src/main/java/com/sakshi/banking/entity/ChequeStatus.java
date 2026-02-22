package com.sakshi.banking.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ChequeStatus {
    ISSUED, CLEARED, BOUNCED;
    @JsonCreator
    public static ChequeStatus from(String value) {
        return ChequeStatus.valueOf(value.toUpperCase());
    }
}
