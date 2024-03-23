package com.org.currency_api.infrastructure.service;

import com.org.currency_api.application.dto.Currencies;
import com.org.currency_api.application.dto.ExchangeRateDto;
import com.org.currency_api.domain.entity.ExchangeRateEntity;
import com.org.currency_api.domain.repository.ExchangeRateEntityRepository;
import com.org.currency_api.infrastructure.external.CurrencyResource;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CurrencyServiceImplTest {

    private CurrencyServiceImpl currencyService;
    private ExchangeRateEntityRepository repository;
    private CurrencyResource currencyResource;
    private EasyRandom generator;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ExchangeRateEntityRepository.class);
        currencyResource = Mockito.mock(CurrencyResource.class);
        generator = new EasyRandom();
    }

    @Test
    void testGetCurrencies() throws NoSuchFieldException, IllegalAccessException {
        //given
        var currency = generator.nextObject(String.class);
        var expectedExchangeDto = new ExchangeRateDto(currency,null,null);

        //when
        currencyService = new CurrencyServiceImpl(repository, currencyResource);
        Field exchangeRateMapField = CurrencyServiceImpl.class.getDeclaredField("exchangeRateMap");
        exchangeRateMapField.setAccessible(true);
        var exchangeRateMap = (Map<String, ExchangeRateDto>) exchangeRateMapField.get(currencyService);
        exchangeRateMap.put(currency,expectedExchangeDto);
        var currencies = currencyService.getCurrencies();

        //then
        assertEquals(new Currencies(Set.of(currency)), currencies);
    }

    @Test
    void testGetExchangeRate() throws NoSuchFieldException, IllegalAccessException {
        //given
        var currency = generator.nextObject(String.class);
        var expectedExchangeDto = new ExchangeRateDto(currency,null,null);

        //when
        currencyService = new CurrencyServiceImpl(repository, currencyResource);

        Field exchangeRateMapField = CurrencyServiceImpl.class.getDeclaredField("exchangeRateMap");
        exchangeRateMapField.setAccessible(true);
        var exchangeRateMap = (Map<String, ExchangeRateDto>) exchangeRateMapField.get(currencyService);
        exchangeRateMap.put(currency,expectedExchangeDto);
        var exchangeRateDto = currencyService.getExchangeRate(currency);

        //then
        assertEquals(expectedExchangeDto, exchangeRateDto);
    }

    @Test
    void testAddCurrencies() {
        //given
        var currency = generator.nextObject(String.class);
        var exchangeDto = new ExchangeRateDto(currency,null,null);

        //when
        when(currencyResource.getExchangeRateUpdate(currency)).thenReturn(exchangeDto);
        when(repository.save(any())).thenReturn(ExchangeRateEntity.builder().currency(currency).build());
        currencyService = new CurrencyServiceImpl(repository, currencyResource);
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
        var updateDto = new ExchangeRateDto(currency,updateDate,null);
        var updatedEntity = ExchangeRateEntity.builder().currency(currency).updateDate(updateDate).build();

        //when
        when(repository.findCurrencies()).thenReturn(List.of(currency));
        when(currencyResource.getExchangeRateUpdate(currency)).thenReturn(updateDto);
        when(repository.findByCurrency(currency)).thenReturn(notUpdatedEntity);
        currencyService = new CurrencyServiceImpl(repository, currencyResource);
        currencyService.updateExchangeRates();

        //then
        verify(repository).save(updatedEntity);
    }
}