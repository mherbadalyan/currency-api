package com.org.currency_api.infrastructure.external;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Map;


public record ExchangeRateResponse(@JsonProperty(value = "base") String currency,
                                   @JsonProperty(value = "date") LocalDateTime updateDate,
                                   Map<String, Double> rates){

}

