package com.hamlet.World.Bank.service;

import com.hamlet.World.Bank.payload.request.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
