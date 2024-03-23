package com.org.currency_api.application.mapper;


import com.org.currency_api.application.dto.ExchangeRateDto;
import com.org.currency_api.domain.entity.ExchangeRateEntity;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

class ExchangeRateMapperTest {
    private EasyRandom generator;


    @BeforeEach
    void init() {
        generator = new EasyRandom();
    }

    @Test
    void shouldConvertDtoToEntity() {
        //given
        var currency = generator.nextObject(String.class);
        var rate = generator.nextObject(String.class);
        var rateValue = generator.nextObject(Double.class);
        var dateTime = generator.nextObject(LocalDateTime.class);
        var dto = new ExchangeRateDto(currency, dateTime, Map.of(rate, rateValue));
        var expectedEntity = ExchangeRateEntity.builder()
                .currency(currency)
                .updateDate(dateTime)
                .rates(Map.of(rate, rateValue))
                .build();

        //when
        var entity = ExchangeRateMapper.toEntity(dto);

        //then
        Assertions.assertEquals(expectedEntity, entity);
    }

    @Test
    void shouldConvertEntityToDto() {
        //given
        var currency = generator.nextObject(String.class);
        var rate = generator.nextObject(String.class);
        var rateValue = generator.nextObject(Double.class);
        var dateTime = generator.nextObject(LocalDateTime.class);
        var entity = ExchangeRateEntity.builder()
                .currency(currency)
                .updateDate(dateTime)
                .rates(Map.of(rate, rateValue))
                .build();
        var expectedDto = new ExchangeRateDto(currency, dateTime, Map.of(rate, rateValue));

        //when
        var dto = ExchangeRateMapper.toDto(entity);

        //then
        Assertions.assertEquals(expectedDto, dto);
    }
}