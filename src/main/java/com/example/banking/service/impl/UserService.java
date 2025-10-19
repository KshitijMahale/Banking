package com.example.banking.service.impl;

import com.example.banking.dto.BankResponse;
import com.example.banking.dto.CreditDebitRequest;
import com.example.banking.dto.EnquiryRequest;
import com.example.banking.dto.UserRequest;

public interface UserService {
    BankResponse craeteAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
}
