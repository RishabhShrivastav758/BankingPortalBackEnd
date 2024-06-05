package com.banking.BankingWebPortal.service;

import com.banking.BankingWebPortal.dto.AccountResponse;
import com.banking.BankingWebPortal.dto.UserResponse;

public interface DashboardService {
        UserResponse getUserDetails(String accountNumber);
        AccountResponse getAccountDetails(String accountNumber);
}
