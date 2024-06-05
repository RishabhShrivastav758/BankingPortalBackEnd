package com.banking.BankingWebPortal.util;

import com.banking.BankingWebPortal.exception.NotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class LoggedInUser {

    public static String getAccountNumber(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            Object principal = authentication.getPrincipal();

            if(principal instanceof User){
                User user = (User) principal;
                return user.getUsername();
            }
        }
        throw new NotFoundException("Account number not found in security context");
    }
}
