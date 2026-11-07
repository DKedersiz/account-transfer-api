package com.dogukan.takehometwo.entity;

import com.dogukan.takehometwo.exception.InsufficientBalanceException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false,name = "account_number")
    private String accountNumber;
    private String username;
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    private BigDecimal balance;

    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be greater than zero");
        }

        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        this.balance = this.balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        this.balance = this.balance.add(amount);
    }
}
