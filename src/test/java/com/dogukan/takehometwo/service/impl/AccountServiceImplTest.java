package com.dogukan.takehometwo.service.impl;

import com.dogukan.takehometwo.dto.request.RequestCreateAccount;
import com.dogukan.takehometwo.entity.Account;
import com.dogukan.takehometwo.mapper.AccountMapper;
import com.dogukan.takehometwo.repository.AccountRepository;
import com.dogukan.takehometwo.service.AccountService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;


    @Test
    void when_createAccount_success() {
        RequestCreateAccount requestCreateAccount = Instancio.create(RequestCreateAccount.class);
        Account account = Instancio.of(Account.class)
                .set(field(Account::getAccountNumber),requestCreateAccount.getAccountNumber())
                .set(field(Account::getUsername),requestCreateAccount.getUsername())
                .set(field(Account::getEmail),requestCreateAccount.getEmail())
                .create();

        when(accountMapper.toEntity(requestCreateAccount)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        assertDoesNotThrow(() -> accountService.createAccount(requestCreateAccount));

        verify(accountMapper, times(1)).toEntity(requestCreateAccount);
        verify(accountRepository, times(1)).save(account);
        verifyNoMoreInteractions(accountMapper, accountRepository);
    }

}