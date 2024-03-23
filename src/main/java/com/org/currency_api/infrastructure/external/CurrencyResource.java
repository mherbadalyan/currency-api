package com.org.currency_api.infrastructure.external;

import com.org.currency_api.application.dto.ExchangeRateDto;

public interface CurrencyResource {
    ExchangeRateDto getExchangeRates(String currencies);
}