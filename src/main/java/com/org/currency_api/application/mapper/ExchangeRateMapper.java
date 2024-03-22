package com.org.currency_api.application.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.org.currency_api.application.dto.ExchangeRateDto;
import com.org.currency_api.domain.entity.ExchangeRateEntity;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ExchangeRateMapper {

    private ExchangeRateMapper() {
    }

    public static ExchangeRateEntity toEntity(ExchangeRateDto dto) {
        return ExchangeRateEntity.builder()
                .currency(dto.getCurrency())
                .updateDate(dto.getUpdateDate())
                .rates(dto.getRates())
                .build();
    }

    public static ExchangeRateDto toDto(ExchangeRateEntity entity) {
        return ExchangeRateDto.builder()
                .currency(entity.getCurrency())
                .updateDate(entity.getUpdateDate())
                .rates(entity.getRates())
                .build();
    }

    public static ExchangeRateDto toDto(JsonNode jsonNode) {
        var zonedDateString = jsonNode.get("date").asText();
        var date = ZonedDateTime.parse(zonedDateString).toLocalDateTime();
        var currency = jsonNode.get("base").asText();
        var rates = toMap(jsonNode.get("rates"));
        return new ExchangeRateDto(currency, date, rates);
    }


    private static Map<String, Double> toMap(JsonNode jsonNode) {
        Map<String, Double> resultMap = new HashMap<>();

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            resultMap.put(field.getKey(), field.getValue().asDouble());
        }
        return resultMap;
    }
}
