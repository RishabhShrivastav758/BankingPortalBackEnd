package com.banking.BankingWebPortal.exception;

public class UnauthorizedException extends RuntimeException{

    private static final long serialVersionUID = -4893320765855582206L;

    public UnauthorizedException(String message){
        super(message);
    }
}
