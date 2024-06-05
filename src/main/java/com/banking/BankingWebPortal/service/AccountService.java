package com.banking.BankingWebPortal.service;

import com.banking.BankingWebPortal.entity.Account;
import com.banking.BankingWebPortal.entity.User;

public interface AccountService {
    public Account createAccount(User user);
    public boolean isPinCreated(String accountNumber);
    public void createPin (String accountNumber, String password, String pin);
    public void updatePin (String accountNumber, String oldPin, String password, String newPin);
    public void cashDeposit (String accountNumber, String pin, double amount);
    public void cashWithdrawal(String accountNumber, String pin, double amount);
    public void fundTransfer(String sourceAccountNumber, String targetAccountNumber, String pin, double amount);
}
