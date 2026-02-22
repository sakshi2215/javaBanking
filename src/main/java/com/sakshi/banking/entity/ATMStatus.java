package com.sakshi.banking.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ATMStatus {
    ACTIVE, INACTIVE, MAINTENANCE;
    @JsonCreator
    public static ATMStatus from(String value) {
        return ATMStatus.valueOf(value.toUpperCase());
    }
}