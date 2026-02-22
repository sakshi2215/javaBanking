package com.sakshi.banking.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CardType {
    DEBIT, CREDIT;
    @JsonCreator
    public static CardType from(String value) {
        return CardType.valueOf(value.toUpperCase());
    }
}
