package com.org.currency_api.application.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {
    private String currency;
    private LocalDateTime updateDate;
    private Map<String, Double> rates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRateDto that = (ExchangeRateDto) o;
        return Objects.equals(currency, that.currency) && Objects.equals(updateDate, that.updateDate) && Objects.equals(rates, that.rates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, updateDate, rates);
    }
}
