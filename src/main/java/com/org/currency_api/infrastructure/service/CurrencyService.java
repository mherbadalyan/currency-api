package com.org.currency_api.infrastructure.service;

import com.org.currency_api.application.dto.Currencies;
import com.org.currency_api.application.dto.ExchangeRateDto;

public interface CurrencyService {
    Currencies getCurrencyList();
    ExchangeRateDto getExchangeRate(String currency);
    String addCurrency(String currency);
}
