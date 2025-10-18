package com.example.banking.service.impl;

import com.example.banking.dto.BankResponse;
import com.example.banking.dto.UserRequest;

public interface UserService {
    BankResponse craeteAccount(UserRequest userRequest);
}
