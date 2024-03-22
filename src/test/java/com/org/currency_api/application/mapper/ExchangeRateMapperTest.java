package com.org.currency_api.application.mapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.org.currency_api.application.dto.ExchangeRateDto;
import com.org.currency_api.domain.entity.ExchangeRateEntity;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;

class ExchangeRateMapperTest {

    public static final String BASE = "base";
    public static final String DATE = "date";
    public static final String RATES = "rates";
    private EasyRandom generator;

    private ObjectMapper mapper;

    @BeforeEach
    void init() {
        generator = new EasyRandom();
        mapper = new ObjectMapper();
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

    @Test
    void shouldConvertJsonToDto() throws IOException {
        //given
        var currency = generator.nextObject(String.class);
        var rate = generator.nextObject(String.class);
        var rateValue = generator.nextObject(Double.class);
        var dateTime = generator.nextObject(ZonedDateTime.class);

        var textNode = TextNode.valueOf(currency);
        var dateNode = TextNode.valueOf(dateTime.toString());
        var rateNode = mapper.createObjectNode();
        rateNode.set(rate, DoubleNode.valueOf(rateValue));

        var json = mapper.createObjectNode();
        json.set(BASE, textNode);
        json.set(DATE, dateNode);
        json.set(RATES, rateNode);

        var expectedDto = new ExchangeRateDto(currency, dateTime.toLocalDateTime(), Map.of(rate, rateValue));

        //when
        var dto = ExchangeRateMapper.toDto(json);

        //then
        Assertions.assertEquals(expectedDto, dto);
    }
}