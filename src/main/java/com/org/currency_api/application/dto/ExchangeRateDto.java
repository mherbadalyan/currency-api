package com.org.currency_api.application.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {
    private String currency;
    private LocalDateTime updateDate;
    private Map<String, Double> rates;
}
