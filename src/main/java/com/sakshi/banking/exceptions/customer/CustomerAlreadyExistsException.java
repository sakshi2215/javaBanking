package com.sakshi.banking.exceptions.customer;

public class CustomerAlreadyExistsException extends RuntimeException
{
    public CustomerAlreadyExistsException(String msg){
        super(msg);
    }
}
