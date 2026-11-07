package com.dogukan.takehometwo.service;

import com.dogukan.takehometwo.dto.request.RequestCreateAccount;
import com.dogukan.takehometwo.dto.request.RequestTransferMoney;
import com.dogukan.takehometwo.dto.request.ResponseBalance;

public interface AccountService {
    void createAccount(RequestCreateAccount requestCreateAccount);
    void transferMoney(RequestTransferMoney requestTransferMoney);
    ResponseBalance getBalance(String accountNumber);
}
