package com.banking.BankingWebPortal.service;

import com.banking.BankingWebPortal.dto.TransactionDTO;
import com.banking.BankingWebPortal.entity.Transaction;
import com.banking.BankingWebPortal.mapper.TransactionMapper;
import com.banking.BankingWebPortal.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;


    @Override
    public List<TransactionDTO> getAllTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions = transactionRepository.findBySourceAccount_AccountNumberOrTargetAccount_AccountNumber(accountNumber, accountNumber);

        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(transactionMapper::toDto)
                .sorted((t1, t2) -> t2.getTransaction_date().compareTo(t1.getTransaction_date()))
                .collect(Collectors.toList());

        return transactionDTOs;
    }
}
