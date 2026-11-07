package com.dogukan.takehometwo.entity;

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

}
