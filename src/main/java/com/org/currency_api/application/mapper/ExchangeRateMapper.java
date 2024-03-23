package com.org.currency_api.application.mapper;

import com.org.currency_api.application.dto.ExchangeRateDto;
import com.org.currency_api.domain.entity.ExchangeRateEntity;
import com.org.currency_api.infrastructure.external.ExchangeRateResponse;

public class ExchangeRateMapper {

    private ExchangeRateMapper() {
    }

    public static ExchangeRateEntity toEntity(ExchangeRateDto dto) {
        return ExchangeRateEntity.builder()
                .currency(dto.currency())
                .updateDate(dto.updateDate())
                .rates(dto.rates())
                .build();
    }

    public static ExchangeRateDto toDto(ExchangeRateEntity entity) {
        return new ExchangeRateDto(entity.getCurrency(), entity.getUpdateDate(), entity.getRates());
    }

    public static ExchangeRateDto toDto(ExchangeRateResponse response) {
        return new ExchangeRateDto(response.currency(), response.updateDate(), response.rates());
    }
}
