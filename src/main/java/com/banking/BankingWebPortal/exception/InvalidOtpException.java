package com.banking.BankingWebPortal.exception;

public class InvalidOtpException extends RuntimeException{

    private static final long serialVersionUID = 9176543614742615822L;

    public InvalidOtpException(String message){
        super(message);
    }
}
