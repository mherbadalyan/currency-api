package com.org.currency_api.infrastructure.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.TextNode;
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
import java.time.ZonedDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExternalResourceImplTest {

    private EasyRandom generator;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        generator = new EasyRandom();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldGetExchangeRateUpdate() throws IOException, InterruptedException {
        //when
        var currency = generator.nextObject(String.class);
        var rate = generator.nextObject(String.class);
        var rateValue = generator.nextObject(Double.class);
        var dateTime = generator.nextObject(ZonedDateTime.class);

        var textNode = TextNode.valueOf(currency);
        var dateNode = TextNode.valueOf(dateTime.toString());
        var rateNode = objectMapper.createObjectNode();
        rateNode.set(rate, DoubleNode.valueOf(rateValue));

        var json = objectMapper.createObjectNode();
        json.set("base", textNode);
        json.set("date", dateNode);
        json.set("rates", rateNode);

        var mockResponseBody = generator.nextObject(String.class);
        var mockUri = "https://api.example.com/rates";
        var mockKey = generator.nextObject(String.class);
        var mockObjectMapper = Mockito.mock(ObjectMapper.class);
        var mockResponse = Mockito.mock(HttpResponse.class);
        var httpClient = Mockito.mock(HttpClient.class);

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass()))).thenReturn(mockResponse);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockObjectMapper.readTree(any(String.class))).thenReturn(json);
        var externalResource = new ExternalResourceImpl(mockObjectMapper, httpClient, mockUri, mockKey);

        var exchangeRateDto = ExchangeRateDto.builder()
                .currency(currency)
                .updateDate(dateTime.toLocalDateTime())
                .rates(Map.of(rate, rateValue))
                .build();
        var response = externalResource.getExchangeRateUpdate(generator.nextObject(String.class));

        // then
        verify(httpClient).send(any(HttpRequest.class), any());
        Assertions.assertEquals(exchangeRateDto, response);
    }

}
