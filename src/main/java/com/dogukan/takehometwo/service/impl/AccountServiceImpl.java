package com.dogukan.takehometwo.service.impl;

import com.dogukan.takehometwo.dto.request.RequestCreateAccount;
import com.dogukan.takehometwo.dto.request.RequestTransferMoney;
import com.dogukan.takehometwo.dto.request.ResponseBalance;
import com.dogukan.takehometwo.entity.Account;
import com.dogukan.takehometwo.exception.AccountNotFound;
import com.dogukan.takehometwo.exception.SameAccountException;
import com.dogukan.takehometwo.mapper.AccountMapper;
import com.dogukan.takehometwo.repository.AccountRepository;
import com.dogukan.takehometwo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public void createAccount(RequestCreateAccount requestCreateAccount) {
        Account entity = accountMapper.toEntity(requestCreateAccount);
        accountRepository.save(entity);
    }

    @Override
    @Transactional
    public void transferMoney(RequestTransferMoney requestTransferMoney) {
        String fromNo = requestTransferMoney.getFromAccountNo();
        String toNo = requestTransferMoney.getToAccountNo();

        if (fromNo.equals(toNo)) {
            throw new SameAccountException("Cannot transfer money to the same account");
        }

        Account fromAccount;
        Account toAccount;

        if (fromNo.compareTo(toNo) < 0) {
            fromAccount = lockAccount(fromNo);
            toAccount = lockAccount(toNo);
        } else {
            toAccount = lockAccount(toNo);
            fromAccount = lockAccount(fromNo);
        }

        fromAccount.withdraw(requestTransferMoney.getAmount());
        toAccount.deposit(requestTransferMoney.getAmount());
    }

    @Override
    public ResponseBalance getBalance(String accountNumber) {
        Account foundAccount = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFound("Account number not found"));
        return ResponseBalance.builder()
                .accountNumber(foundAccount.getAccountNumber())
                .amount(foundAccount.getBalance())
                .build();
    }

    private Account lockAccount(String accountNumber) {
        return accountRepository.findForUpdateByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFound("Account number not found: " + accountNumber));
    }


}
