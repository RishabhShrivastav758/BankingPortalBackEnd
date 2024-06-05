package com.banking.BankingWebPortal.mapper;

import com.banking.BankingWebPortal.dto.TransactionDTO;
import com.banking.BankingWebPortal.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDTO toDto(Transaction transaction){

        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setTransaction_type(transaction.getTransactionType());
        dto.setTransaction_date(transaction.getTransaction_date());
        dto.setSourceAccountNumber(transaction.getSourceAccount().getAccountNumber());

        if(transaction.getTargetAccount() != null){
            dto.setTargetAccountNumber(transaction.getTargetAccount().getAccountNumber());
        }else{
            dto.setTargetAccountNumber("-");   // random target account number
        }

        return dto;
    }
}
