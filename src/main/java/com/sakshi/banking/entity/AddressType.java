package com.sakshi.banking.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AddressType {

    HOME,
    OFFICE,
    BILLING,
    CORRESPONDENCE;
    @JsonCreator
    public static AddressType from(String value) {
        return AddressType.valueOf(value.toUpperCase());
    }

}
