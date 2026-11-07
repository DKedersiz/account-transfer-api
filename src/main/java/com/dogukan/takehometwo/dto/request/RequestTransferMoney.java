package com.dogukan.takehometwo.dto.request;

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
    public String fromAccountNo;
    public String toAccountNo;
    @NotNull(message = "Tutar boş olamaz")
    @Positive(message = "Tutar sıfırdan büyük olmalıdır")
    public BigDecimal amount;
}
