package com.dogukan.takehometwo.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ResponseBalance {
    private String accountNumber;
    private BigDecimal amount;
}
