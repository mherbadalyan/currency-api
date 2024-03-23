package com.org.currency_api.infrastructure.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.currency_api.application.dto.ExchangeRateDto;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExternalCurrencyResourceTest {
    private EasyRandom generator;

    @BeforeEach
    void init() {
        generator = new EasyRandom();
    }

    @Test
    void shouldGetExchangeRateUpdate() throws IOException, InterruptedException {
        //given
        var currency = generator.nextObject(String.class);
        var rate = generator.nextObject(String.class);
        var rateValue = generator.nextObject(Double.class);
        var dateTime = generator.nextObject(LocalDateTime.class);
        var exchangeRateResponse = new ExchangeRateResponse(currency, dateTime, Map.of(rate, rateValue));

        var mockResponseBody = generator.nextObject(String.class);
        var mockUri = "https://api.example.com/rates";
        var mockKey = generator.nextObject(String.class);
        var mockObjectMapper = Mockito.mock(ObjectMapper.class);
        var mockResponse = Mockito.mock(HttpResponse.class);
        var httpClient = Mockito.mock(HttpClient.class);

        //when
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass()))).thenReturn(mockResponse);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockObjectMapper.readValue(mockResponseBody, ExchangeRateResponse.class)).thenReturn(exchangeRateResponse);
        var externalResource = new ExternalCurrencyResource(mockObjectMapper, httpClient, mockUri, mockKey);

        var exchangeRateDto = new ExchangeRateDto(currency, dateTime, Map.of(rate, rateValue));
        var response = externalResource.getExchangeRates(generator.nextObject(String.class));

        // then
        verify(httpClient).send(any(HttpRequest.class), any());
        Assertions.assertEquals(exchangeRateDto, response);
    }
}
