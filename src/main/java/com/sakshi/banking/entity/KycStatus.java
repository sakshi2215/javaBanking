package com.sakshi.banking.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum KycStatus {
    PENDING, REJECTED, VERIFIED, UNDER_REVIEW;
    @JsonCreator
    public static KycStatus from(String value) {
        return KycStatus.valueOf(value.toUpperCase());
    }
}
