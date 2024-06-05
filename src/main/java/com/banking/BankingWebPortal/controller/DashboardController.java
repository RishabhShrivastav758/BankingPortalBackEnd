package com.banking.BankingWebPortal.controller;

import com.banking.BankingWebPortal.dto.AccountResponse;
import com.banking.BankingWebPortal.dto.UserResponse;
import com.banking.BankingWebPortal.service.DashboardService;
import com.banking.BankingWebPortal.util.LoggedInUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUserDetails() {
        String accountNumber = LoggedInUser.getAccountNumber();
        UserResponse userResponse = dashboardService.getUserDetails(accountNumber);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/account")
    public ResponseEntity<AccountResponse> getAccountDetails() {
        String accountNumber = LoggedInUser.getAccountNumber();
        AccountResponse accountResponse = dashboardService.getAccountDetails(accountNumber);
        return ResponseEntity.ok(accountResponse);
    }
}
