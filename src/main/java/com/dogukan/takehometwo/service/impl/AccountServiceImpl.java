package com.dogukan.takehometwo.service.impl;

import com.dogukan.takehometwo.dto.request.RequestCreateAccount;
import com.dogukan.takehometwo.dto.request.RequestTransferMoney;
import com.dogukan.takehometwo.dto.request.ResponseBalance;
import com.dogukan.takehometwo.entity.Account;
import com.dogukan.takehometwo.exception.AccountNotFound;
import com.dogukan.takehometwo.exception.InsufficientBalanceException;
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
        Account fromAccount = accountRepository.findByAccountNumber(requestTransferMoney.fromAccountNo)
                .orElseThrow(() -> new AccountNotFound("Account number not found"));

        if (fromAccount.getBalance().compareTo(requestTransferMoney.amount) < 0 ) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        Account toAccount = accountRepository.findByAccountNumber(requestTransferMoney.toAccountNo)
                .orElseThrow(() -> new AccountNotFound("Account number not found"));

        accountRepository.increaseBalance(requestTransferMoney.getAmount(), toAccount.getAccountNumber());
        accountRepository.decreaseBalance(requestTransferMoney.getAmount(), fromAccount.getAccountNumber());
    }

    @Override
    public ResponseBalance getBalance(String accountNumber) {
        Account foundAccount = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFound("Account number not found"));
        return ResponseBalance.builder()
                .accountNumber(foundAccount.getAccountNumber())
                .amount(foundAccount.getBalance())
                .build();
    }


}
