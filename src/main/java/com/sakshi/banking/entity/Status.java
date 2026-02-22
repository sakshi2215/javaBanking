package com.sakshi.banking.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    ACTIVE, INACTIVE, BLOCKED, CLOSED;
    @JsonCreator
    public static Status from(String value) {
        return Status.valueOf(value.toUpperCase());
    }
}
