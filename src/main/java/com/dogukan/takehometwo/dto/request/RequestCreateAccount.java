package com.dogukan.takehometwo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateAccount {
    @NotNull(message = "Account number can not be null.")
    private String accountNumber;
    @NotNull(message = "Username can not be null.")
    private String username;
    @NotNull(message = "Password can not be null.")
    private String password;
    @Email
    private String email;
    @NotNull(message = "Balance can not be null.")
    private BigDecimal balance;
}
