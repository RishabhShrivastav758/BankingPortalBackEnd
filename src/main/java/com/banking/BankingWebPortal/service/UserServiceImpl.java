package com.banking.BankingWebPortal.service;

import com.banking.BankingWebPortal.entity.Account;
import com.banking.BankingWebPortal.entity.User;
import com.banking.BankingWebPortal.exception.UserValidation;
import com.banking.BankingWebPortal.repository.UserRepository;
import com.banking.BankingWebPortal.util.LoggedInUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userInfoRepository;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userInfoRepository, AccountService accountService,PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.accountService = accountService;
        this.passwordEncoder =  passwordEncoder;
    }

    @Override
    public User registerUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Save the user details
        User savedUser = userInfoRepository.save(user);

        // Create an account for the user
        Account account = accountService.createAccount(savedUser);

        savedUser.setAccount(account);
        userInfoRepository.save(savedUser);

        System.out.println(savedUser.getAccount().getAccountNumber());
        System.out.println(account.getUser().getName());


        return savedUser;
    }

    @Override
    public void saveUser(User user) {
        userInfoRepository.save(user);
    }

    @Override
    public User getUserByAccountNumber(String accountNumber) {
        return userInfoRepository.findByAccountAccountNumber(accountNumber);
    }

    @Override
    public User updateUser(User user) {
        User existingUser = userInfoRepository.findByAccountAccountNumber(LoggedInUser.getAccountNumber());
        if(user.getEmail() != null){
            if(user.getEmail().isEmpty())
                throw new UserValidation("Email can't be empty");
            else
                existingUser.setEmail(user.getEmail());
        }
        if(user.getName() != null){
            if(user.getName().isEmpty())
                throw new UserValidation("Name can't be empty");
            else
                existingUser.setName(user.getName());
        }
        if(user.getPhone_number() != null){
            if(user.getPhone_number().isEmpty())
                throw new UserValidation("Phone number can't be empty");
            else
                existingUser.setPhone_number(user.getPhone_number());
        }
        if(user.getAddress() != null){
            existingUser.setAddress(user.getAddress());
        }
        return userInfoRepository.save(existingUser);
    }
}
