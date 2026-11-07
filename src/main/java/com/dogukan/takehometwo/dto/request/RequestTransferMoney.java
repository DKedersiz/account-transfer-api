package com.dogukan.takehometwo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestTransferMoney {
    @NotBlank(message = "Gönderici hesap numarası gerekli")
    private String fromAccountNo;
    @NotBlank(message = "Alıcı hesap numarası gerekli")
    private String toAccountNo;
    @NotNull(message = "Tutar boş olamaz")
    @Positive(message = "Tutar sıfırdan büyük olmalıdır")
    private BigDecimal amount;
}
