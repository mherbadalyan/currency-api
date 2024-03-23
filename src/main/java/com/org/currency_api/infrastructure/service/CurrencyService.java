package com.org.currency_api.infrastructure.service;

import com.org.currency_api.application.dto.Currencies;
import com.org.currency_api.application.dto.ExchangeRateDto;

public interface CurrencyService {
    Currencies getCurrencies();
    ExchangeRateDto getExchangeRate(String currency);
    ExchangeRateDto addCurrency(String currency);
}
