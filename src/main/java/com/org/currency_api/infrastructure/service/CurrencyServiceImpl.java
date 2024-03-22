package com.org.currency_api.infrastructure.service;

import com.org.currency_api.application.dto.Currencies;
import com.org.currency_api.application.dto.ExchangeRateDto;
import com.org.currency_api.domain.entity.ExchangeRateEntity;
import com.org.currency_api.domain.repository.ExchangeRateEntityRepository;
import com.org.currency_api.infrastructure.external.ExternalResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.org.currency_api.application.mapper.ExchangeRateMapper.toDto;
import static com.org.currency_api.application.mapper.ExchangeRateMapper.toEntity;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final Map<String, ExchangeRateDto> exchangeRateMap;
    private final ExchangeRateEntityRepository repository;
    private final ExternalResource externalResourceImpl;


    public CurrencyServiceImpl(ExchangeRateEntityRepository repository, ExternalResource externalResourceImpl) {
        this.repository = repository;
        this.externalResourceImpl = externalResourceImpl;
        this.exchangeRateMap = initFromDb();
    }

    @Override
    public Currencies getCurrencies() {
        var availableCurrencies = exchangeRateMap.keySet();
        if (availableCurrencies.isEmpty()) {
            throw new RuntimeException("There are no available currencies, please add currency.");
        }
        return new Currencies(availableCurrencies);
    }

    @Override
    public ExchangeRateDto getExchangeRate(String currency) {
        if (!exchangeRateMap.containsKey(currency)) {
            throw new RuntimeException("There are no available currency %s".formatted(currency));
        }
        return exchangeRateMap.get(currency);
    }

    @Override
    public String addCurrency(String currency) {
        if (exchangeRateMap.containsKey(currency)) {
            throw new RuntimeException("Currency %s is exist in available currencies.".formatted(currency));
        }
        var exchangeRate = externalResourceImpl.getExchangeRateUpdate(currency);
        var savedEntity = repository.save(toEntity(exchangeRate));
        exchangeRateMap.put(exchangeRate.getCurrency(), exchangeRate);
        return savedEntity.getCurrency();
    }

    private Map<String, ExchangeRateDto> initFromDb() {
        var map = new ConcurrentHashMap<String, ExchangeRateDto>();
        var exchangeRateEntities = repository.findAll();
        if (!exchangeRateEntities.isEmpty()) {
            for (ExchangeRateEntity exchangeRateEntity : exchangeRateEntities) {
                var exchangeRate = toDto(exchangeRateEntity);
                map.put(exchangeRate.getCurrency(), exchangeRate);
            }
        }
        return map;
    }

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedRate = 1)
    public void updateExchangeRates() {
        var currencies = repository.findCurrencies();

        var updatedMap = currencies.stream()
                .map(externalResourceImpl::getExchangeRateUpdate)
                .collect(Collectors.toMap(ExchangeRateDto::getCurrency, dto -> dto));

        for (Map.Entry<String, ExchangeRateDto> entry : updatedMap.entrySet()) {
            var updatedDto = entry.getValue();
            var currency = entry.getKey();
            var entity = repository.findByCurrency(currency);
            entity.setRates(updatedDto.getRates());
            entity.setUpdateDate(updatedDto.getUpdateDate());
            repository.save(entity);
            exchangeRateMap.put(currency, updatedDto);
        }
    }
}
