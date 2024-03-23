package com.org.currency_api.application.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ExchangeRateDto(String currency, LocalDateTime updateDate, Map<String, Double> rates) {
}