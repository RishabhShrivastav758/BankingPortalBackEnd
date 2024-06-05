package com.banking.BankingWebPortal.controller;

import com.banking.BankingWebPortal.dto.*;
import com.banking.BankingWebPortal.service.AccountService;
import com.banking.BankingWebPortal.service.TransactionService;
import com.banking.BankingWebPortal.util.LoggedInUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/pin/check")
    public ResponseEntity<?> checkAccountPin(){
        boolean isPinValid = accountService.isPinCreated(LoggedInUser.getAccountNumber());

        Map<String, Object> result = new HashMap<>();
        result.put("hasPin", isPinValid);

        if(isPinValid){
            result.put("msg", "Pin Created");
        }else{
            result.put("msg", "Pin not Created");
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/pin/create")
    public ResponseEntity<?> createPIN(@RequestBody PinRequest pinRequest) {
        accountService.createPin(LoggedInUser.getAccountNumber(), pinRequest.getPassword(), pinRequest.getPin());

        Map<String, String> response = new HashMap<>();
        response.put("msg", "PIN created successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/pin/update")
    public ResponseEntity<?> updatePIN(@RequestBody PinUpdateRequest pinUpdateRequest) {
        accountService.updatePin(LoggedInUser.getAccountNumber(), pinUpdateRequest.getOldPin(),
                pinUpdateRequest.getPassword(), pinUpdateRequest.getNewPin());

        Map<String, String> response = new HashMap<>();
        response.put("msg", "PIN updated successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/deposit")
    public ResponseEntity<?> cashDeposit(@RequestBody AmountRequest amountRequest) {

        if (amountRequest.getAmount() <= 0) {
            Map<String, String> error = new HashMap<>();
            error.put("Error", "Invalid amount");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        accountService.cashDeposit(LoggedInUser.getAccountNumber(), amountRequest.getPin(), amountRequest.getAmount());

        Map<String, String> response = new HashMap<>();
        response.put("msg", "Cash deposited successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> cashWithdrawal(@RequestBody AmountRequest amountRequest) {

        if (amountRequest.getAmount() <= 0) {
            Map<String, String> error = new HashMap<>();
            error.put("Error", "Invalid amount");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        accountService.cashWithdrawal(LoggedInUser.getAccountNumber(), amountRequest.getPin(),
                amountRequest.getAmount());

        Map<String, String> response = new HashMap<>();
        response.put("msg", "Cash withdrawn successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/fund-transfer")
    public ResponseEntity<?> fundTransfer(@RequestBody FundTransferRequest fundTransferRequest) {

        if (fundTransferRequest.getAmount() <= 0) {
            Map<String, String> err = new HashMap<>();
            err.put("Error", "Invalid amount");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        accountService.fundTransfer(LoggedInUser.getAccountNumber(), fundTransferRequest.getTargetAccountNumber(),
                fundTransferRequest.getPin(), fundTransferRequest.getAmount());
        Map<String, String> response = new HashMap<>();
        response.put("msg", "Fund transferred successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsByAccountNumber() {
        List<TransactionDTO> transactions = transactionService
                .getAllTransactionsByAccountNumber(LoggedInUser.getAccountNumber());
        return ResponseEntity.ok(transactions);
    }

}
