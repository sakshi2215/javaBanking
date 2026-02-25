package com.sakshi.banking.utility;
import java.util.UUID;

public class ReferenceNumberGenerator {
    public String generateReferenceNumber(){
        UUID randomUUid = UUID.randomUUID();
        return randomUUid.toString();
    }

}
