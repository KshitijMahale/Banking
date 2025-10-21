package com.example.banking.service.impl;

import com.example.banking.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
    void sendEmailWithAttachment(EmailDetails emailDetails);
}
