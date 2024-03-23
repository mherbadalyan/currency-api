package com.org.currency_api.infrastructure.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {
    @JsonProperty(value = "base")
    private String currency;
    @JsonProperty(value = "date")
    private LocalDateTime updateDate;
    private Map<String, Double> rates;
}
