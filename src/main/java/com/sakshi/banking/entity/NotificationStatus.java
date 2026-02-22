package com.sakshi.banking.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NotificationStatus {
    SENT, READ, FAILED;
    @JsonCreator
    public static NotificationStatus from(String value) {
        return NotificationStatus.valueOf(value.toUpperCase());
    }
}