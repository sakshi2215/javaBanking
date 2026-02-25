package com.sakshi.banking.exceptions.customer;

public class CustomerInactiveException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public CustomerInactiveException(String msg) {
        super(msg);
    }
}
