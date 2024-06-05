package com.banking.BankingWebPortal.service;

import com.banking.BankingWebPortal.entity.User;

public interface UserService {

    public User registerUser(User user);

    User getUserByAccountNumber(String accountNumber);

    public void saveUser(User user);

    User updateUser(User user);
}
