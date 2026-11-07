package com.dogukan.takehometwo.service.impl;

import com.dogukan.takehometwo.dto.request.RequestCreateAccount;
import com.dogukan.takehometwo.dto.request.RequestTransferMoney;
import com.dogukan.takehometwo.entity.Account;
import com.dogukan.takehometwo.exception.InsufficientBalanceException;
import com.dogukan.takehometwo.exception.SameAccountException;
import com.dogukan.takehometwo.mapper.AccountMapper;
import com.dogukan.takehometwo.repository.AccountRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;


    @Test
    void when_createAccount_success() {
        RequestCreateAccount requestCreateAccount = Instancio.create(RequestCreateAccount.class);
        Account account = Instancio.of(Account.class)
                .set(field(Account::getAccountNumber),requestCreateAccount.getAccountNumber())
                .set(field(Account::getUsername),requestCreateAccount.getUsername())
                .set(field(Account::getEmail),requestCreateAccount.getEmail())
                .create();

        when(accountMapper.toEntity(requestCreateAccount)).thenReturn(account);
        assertDoesNotThrow(() -> accountService.createAccount(requestCreateAccount));

        verify(accountMapper, times(1)).toEntity(requestCreateAccount);
        verify(accountRepository, times(1)).save(account);
        verifyNoMoreInteractions(accountMapper, accountRepository);
    }

    @Test
    void transferMoney_movesMoneyFromSenderToReceiver() {
        // Arrange: sahneyi kur
        Account from = Account.builder().accountNumber("A").balance(new BigDecimal("100")).build();
        Account to = Account.builder().accountNumber("B").balance(new BigDecimal("50")).build();
        when(accountRepository.findForUpdateByAccountNumber("A")).thenReturn(Optional.of(from));
        when(accountRepository.findForUpdateByAccountNumber("B")).thenReturn(Optional.of(to));

        RequestTransferMoney request = RequestTransferMoney.builder()
                .fromAccountNo("A")
                .toAccountNo("B")
                .amount(new BigDecimal("30"))
                .build();

        // Act: tek bir çağrı, test ettiğin şey
        accountService.transferMoney(request);

        // Assert: sonuç doğru mu?
        assertEquals(new BigDecimal("70"), from.getBalance());
        assertEquals(new BigDecimal("80"), to.getBalance());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void transferMoney_whenSameAccount_shouldThrowException() {
        // Arrange: sahneyi kur
        RequestTransferMoney request = Instancio.of(RequestTransferMoney.class)
                .set(field(RequestTransferMoney::getFromAccountNo),"A")
                .set(field(RequestTransferMoney::getToAccountNo),"A")
                .create();


        // Act: tek bir çağrı, test ettiğin şey
        assertThrows(SameAccountException.class, () -> accountService.transferMoney(request));

        // Assert: sonuç doğru mu?
        verifyNoInteractions(accountRepository);
    }

    @Test
    void transferMoney_whenInsufficientBalance_shouldThrowException() {
        // Arrange: sahneyi kur
        Account fromAccount = Instancio.of(Account.class)
                .set(field(Account::getBalance),new BigDecimal("100"))
                .set(field(Account::getAccountNumber),"A")
                .create();

        Account toAccount = Instancio.of(Account.class)
                .set(field(Account::getAccountNumber),"B")
                .set(field(Account::getBalance),new BigDecimal("50"))
                .create();

        RequestTransferMoney requestTransferMoney = Instancio.of(RequestTransferMoney.class)
                .set(field(RequestTransferMoney::getAmount),new BigDecimal("200"))
                .set(field(RequestTransferMoney::getFromAccountNo),"A")
                .set(field(RequestTransferMoney::getToAccountNo),"B")
                .create();

        when(accountRepository.findForUpdateByAccountNumber("A")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findForUpdateByAccountNumber("B")).thenReturn(Optional.of(toAccount));
        // Act: tek bir çağrı, test ettiğin şey
        assertThrows(InsufficientBalanceException.class, () -> accountService.transferMoney(requestTransferMoney));

        // Assert: sonuç doğru mu
        assertEquals(new BigDecimal("100"), fromAccount.getBalance());
        assertEquals(new BigDecimal("50"), toAccount.getBalance());
    }

}