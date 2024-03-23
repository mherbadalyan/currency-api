package com.org.currency_api.infrastructure.service;

import com.org.currency_api.application.dto.Currencies;
import com.org.currency_api.application.dto.ExchangeRateDto;
import com.org.currency_api.domain.entity.ExchangeRateEntity;
import com.org.currency_api.domain.repository.ExchangeRateEntityRepository;
import com.org.currency_api.infrastructure.external.CurrencyResource;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.org.currency_api.application.mapper.ExchangeRateMapper.toDto;
import static com.org.currency_api.application.mapper.ExchangeRateMapper.toEntity;

@Service
public class CurrencyServiceImpl implements CurrencyService, ApplicationListener<ContextRefreshedEvent> {

    private final Map<String, ExchangeRateDto> exchangeRateMap;
    private final ExchangeRateEntityRepository repository;
    private final CurrencyResource currencyResource;


    public CurrencyServiceImpl(ExchangeRateEntityRepository repository, CurrencyResource currencyResource) {
        this.repository = repository;
        this.currencyResource = currencyResource;
        this.exchangeRateMap = new ConcurrentHashMap<>();
    }

    @Override
    public Currencies getCurrencies() {
        var availableCurrencies = exchangeRateMap.keySet();
        return new Currencies(availableCurrencies);
    }

    @Override
    public ExchangeRateDto getExchangeRate(String currency) {
        if (!exchangeRateMap.containsKey(currency)) {
            throw new RuntimeException("There is no available currency %s".formatted(currency));
        }
        return exchangeRateMap.get(currency);
    }

    @Override
    public ExchangeRateDto addCurrency(String currency) {
        if (exchangeRateMap.containsKey(currency)) {
            throw new RuntimeException("Currency %s exists in available currencies.".formatted(currency));
        }
        var exchangeRate = currencyResource.getExchangeRates(currency);
        var savedEntity = repository.save(toEntity(exchangeRate));
        exchangeRateMap.put(exchangeRate.currency(), exchangeRate);
        return exchangeRate;
    }

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 1)
    public void updateExchangeRates() {
        var currencies = repository.findCurrencies();

        var updatedMap = currencies.stream()
                .map(currencyResource::getExchangeRates)
                .collect(Collectors.toMap(ExchangeRateDto::currency, dto -> dto));

        for (Map.Entry<String, ExchangeRateDto> entry : updatedMap.entrySet()) {
            var updatedDto = entry.getValue();
            var currency = entry.getKey();
            var entity = repository.findByCurrency(currency);
            entity.setRates(updatedDto.rates());
            entity.setUpdateDate(updatedDto.updateDate());
            repository.save(entity);
            exchangeRateMap.put(currency, updatedDto);
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initFromDb();
    }

    private void initFromDb() {
        var exchangeRateEntities = repository.findAll();
        for (ExchangeRateEntity exchangeRateEntity : exchangeRateEntities) {
            var exchangeRate = toDto(exchangeRateEntity);
            exchangeRateMap.put(exchangeRate.currency(), exchangeRate);
        }
    }
}
