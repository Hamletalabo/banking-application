package com.hamlet.World.Bank.service;

import com.hamlet.World.Bank.payload.request.CreditAndDebitRequest;
import com.hamlet.World.Bank.payload.request.EnquiryRequest;
import com.hamlet.World.Bank.payload.request.TransferRequest;
import com.hamlet.World.Bank.payload.response.BankResponse;

public interface UserService {
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditAndDebitRequest creditAndDebitRequest);

    BankResponse debitAccount(CreditAndDebitRequest creditAndDebitRequest);

    //within the same bank
    BankResponse transferMoney(TransferRequest transferRequest);
}
