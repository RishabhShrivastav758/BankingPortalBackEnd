package com.banking.BankingWebPortal.service;

import com.banking.BankingWebPortal.entity.Account;
import com.banking.BankingWebPortal.entity.Transaction;
import com.banking.BankingWebPortal.entity.TransactionType;
import com.banking.BankingWebPortal.entity.User;
import com.banking.BankingWebPortal.exception.InsufficientBalanceException;
import com.banking.BankingWebPortal.exception.NotFoundException;
import com.banking.BankingWebPortal.exception.UnauthorizedException;
import com.banking.BankingWebPortal.repository.AccountRepository;
import com.banking.BankingWebPortal.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Account createAccount(User user) {

        String accountNumber = generateUniqueAccountNumber();
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(0.0);
        return accountRepository.save(account);
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            // Generate a UUID as the account number
            accountNumber = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        } while (accountRepository.findByAccountNumber(accountNumber) != null);

        return accountNumber;
    }

    @Override
    public boolean isPinCreated(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Account not found");
        }

        return account.getPin()!=null;
    }

    @Override
    public void createPin(String accountNumber, String password, String pin) {

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Account not found");
        }

        if (!passwordEncoder.matches(password, account.getUser().getPassword())) {
            throw new UnauthorizedException("Invalid password");
        }

        account.setPin(passwordEncoder.encode(pin));
        accountRepository.save(account);
    }

    @Override
    public void updatePin(String accountNumber, String password,  String oldPin, String newPin) {

        System.out.println(accountNumber+"  "+oldPin+" "+newPin+"  "+password);

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Account not found");
        }

        if (!passwordEncoder.matches(oldPin, account.getPin())) {
            throw new UnauthorizedException("Invalid PIN");
        }

        if (!passwordEncoder.matches(password, account.getUser().getPassword())) {
            throw new UnauthorizedException("Invalid password");
        }

        account.setPin(passwordEncoder.encode(newPin));
        accountRepository.save(account);
    }

    @Override
    public void cashDeposit(String accountNumber, String pin, double amount) {

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Account not found");
        }

        if (!passwordEncoder.matches(pin, account.getPin())) {
            throw new UnauthorizedException("Invalid PIN");
        }

        double currentBalance = account.getBalance();
        double newBalance = currentBalance + amount;
        account.setBalance(newBalance);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.CASH_DEPOSIT);;
        transaction.setTransaction_date(new Date());
        transaction.setSourceAccount(account);
        transactionRepository.save(transaction);
    }

    @Override
    public void cashWithdrawal(String accountNumber, String pin, double amount) {

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Account not found");
        }

        if (!passwordEncoder.matches(pin, account.getPin())) {
            throw new UnauthorizedException("Invalid PIN");
        }

        double currentBalance = account.getBalance();
        if (currentBalance < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        double newBalance = currentBalance - amount;
        account.setBalance(newBalance);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.CASH_WITHDRAWAL);
        transaction.setTransaction_date(new Date());
        transaction.setSourceAccount(account);
        transactionRepository.save(transaction);
    }

    @Override
    public void fundTransfer(String sourceAccountNumber, String targetAccountNumber, String pin, double amount) {

        Account sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber);
        if (sourceAccount == null) {
            throw new NotFoundException("Source account not found");
        }

        Account targetAccount = accountRepository.findByAccountNumber(targetAccountNumber);
        if (targetAccount == null) {
            throw new NotFoundException("Target account not found");
        }

        if (!passwordEncoder.matches(pin, sourceAccount.getPin())) {
            throw new UnauthorizedException("Invalid PIN");
        }

        double sourceBalance = sourceAccount.getBalance();
        if (sourceBalance < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        double newSourceBalance = sourceBalance - amount;
        sourceAccount.setBalance(newSourceBalance);
        accountRepository.save(sourceAccount);

        double targetBalance = targetAccount.getBalance();
        double newTargetBalance = targetBalance + amount;
        targetAccount.setBalance(newTargetBalance);
        accountRepository.save(targetAccount);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.CASH_TRANSFER);
        transaction.setTransaction_date(new Date());
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transactionRepository.save(transaction);
    }
}
