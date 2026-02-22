package com.sakshi.banking.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AccountType {
        SAVINGS, CURRENT, FIXED;
        @JsonCreator
        public static AccountType from(String value) {
                return AccountType.valueOf(value.toUpperCase());
        }
}
