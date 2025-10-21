package com.example.banking.controller;

import com.example.banking.entity.Transaction;
import com.example.banking.service.impl.BankStatement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/bankStatement")
public class TransactionController {
    @Autowired
    BankStatement bankStatement;

    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate) {
        return bankStatement.generateStatement(accountNumber, startDate, endDate);
    }
}
