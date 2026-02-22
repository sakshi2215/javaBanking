package com.sakshi.banking.exceptions;
import java.util.Date;

public class ErrorMessage {
    private int statusCode;
    private Date timeStamp;
    private String message;
    private String description;

    public ErrorMessage(int statusCode, Date timeStamp, String message, String description){
        this.statusCode = statusCode;
        this.message = message;
        this.timeStamp = timeStamp;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
