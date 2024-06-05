package com.banking.BankingWebPortal.service;

import com.banking.BankingWebPortal.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {

    List<TransactionDTO> getAllTransactionsByAccountNumber(String accountNumber);

}
