package com.org.currency_api.infrastructure.service;

import com.org.currency_api.application.dto.Currencies;
import com.org.currency_api.application.dto.ExchangeRateDto;
import com.org.currency_api.domain.entity.ExchangeRateEntity;
import com.org.currency_api.domain.repository.ExchangeRateEntityRepository;
import com.org.currency_api.infrastructure.external.ExternalResource;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CurrencyServiceImplTest {

    private CurrencyServiceImpl currencyService;
    private ExchangeRateEntityRepository repository;
    private ExternalResource externalResource;
    private EasyRandom generator;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ExchangeRateEntityRepository.class);
        externalResource = Mockito.mock(ExternalResource.class);
        generator = new EasyRandom();
    }

    @Test
    void testGetCurrencies() {
        //given
        var currency = generator.nextObject(String.class);
        var entity = ExchangeRateEntity.builder().currency(currency).build();

        //when
        when(repository.findAll()).thenReturn(List.of(entity));
        currencyService = new CurrencyServiceImpl(repository, externalResource);
        var currencies = currencyService.getCurrencies();

        //then
        assertEquals(new Currencies(Set.of(currency)), currencies);
    }

    @Test
    void testGetExchangeRate() {
        //given
        var currency = generator.nextObject(String.class);
        var entity = ExchangeRateEntity.builder().currency(currency).build();
        var expectedExchangeDto = ExchangeRateDto.builder().currency(currency).build();

        //when
        when(repository.findAll()).thenReturn(List.of(entity));
        currencyService = new CurrencyServiceImpl(repository, externalResource);
        var exchangeRate = currencyService.getExchangeRate(currency);

        //then
        assertEquals(expectedExchangeDto, exchangeRate);
    }

    @Test
    void testAddCurrencies() {
        //given
        var currency = generator.nextObject(String.class);
        var exchangeDto = ExchangeRateDto.builder().currency(currency).build();

        //when
        when(externalResource.getExchangeRateUpdate(currency)).thenReturn(exchangeDto);
        when(repository.save(any())).thenReturn(ExchangeRateEntity.builder().currency(currency).build());
        currencyService = new CurrencyServiceImpl(repository, externalResource);
        var savedCurrency = currencyService.addCurrency(currency);

        //then
        assertEquals(currency, savedCurrency);
    }

    @Test
    void UpdateExchangeRates() {
        //given
        var currency = generator.nextObject(String.class);
        var updateDate = generator.nextObject(LocalDateTime.class);
        var notUpdatedEntity = ExchangeRateEntity.builder().currency(currency).build();
        var updateDto = ExchangeRateDto.builder().currency(currency).updateDate(updateDate).build();
        var updatedEntity = ExchangeRateEntity.builder().currency(currency).updateDate(updateDate).build();

        //when
        when(repository.findCurrencies()).thenReturn(List.of(currency));
        when(externalResource.getExchangeRateUpdate(currency)).thenReturn(updateDto);
        when(repository.findByCurrency(currency)).thenReturn(notUpdatedEntity);
        currencyService = new CurrencyServiceImpl(repository, externalResource);
        currencyService.updateExchangeRates();

        //then
        verify(repository).save(updatedEntity);
    }
}