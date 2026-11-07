package com.dogukan.takehometwo.dto.request;

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
    public BigDecimal amount;
}
