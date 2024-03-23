package com.org.currency_api.infrastructure.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.currency_api.application.dto.ExchangeRateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.org.currency_api.application.mapper.ExchangeRateMapper.toDto;

@Component
public class ExternalCurrencyResource implements CurrencyResource {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final String uri;
    private final String key;

    public ExternalCurrencyResource(ObjectMapper objectMapper, HttpClient httpClient,
                                    @Value(value = "${web.api.uri}")
                                    String uri,
                                    @Value(value = "${web.api.key}")
                                    String key) {
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
        this.uri = uri;
        this.key = key;
    }

    @Override
    public ExchangeRateDto getExchangeRates(String currency) {
        try {
            HttpResponse<String> send = httpClient.send(HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(uri.formatted(currency, key)))
                    .build(), HttpResponse.BodyHandlers.ofString());
            var body = send.body();
            var exchangeRateResponse = objectMapper.readValue(body, ExchangeRateResponse.class);
            return toDto(exchangeRateResponse);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Exchange rate fetching error.", e);
        }
    }
}
