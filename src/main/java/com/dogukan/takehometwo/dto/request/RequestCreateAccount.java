package com.dogukan.takehometwo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateAccount {
    @NotBlank(message = "Account number is required")
    private String accountNumber;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;
    @NotNull(message = "Balance is required")
    @PositiveOrZero(message = "Initial balance cannot be negative")
    private BigDecimal balance;
}
