package com.org.currency_api.infrastructure.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.currency_api.application.dto.ExchangeRateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.org.currency_api.application.mapper.ExchangeRateMapper.toDto;

@Service
public class ExternalResourceImpl implements ExternalResource {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Value(value = "${web.api.uri}")
    private String uri;

    @Value(value = "${web.api.key}")
    private String key;

    public ExternalResourceImpl(ObjectMapper objectMapper, HttpClient httpClient) {
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
    }


    @Override
    public ExchangeRateDto getExchangeRate(String currency) {
        var jsonNode = doGet(currency);
        return toDto(jsonNode);
    }

    JsonNode doGet(String currency) {
        try {
            var body = httpClient.send(HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(uri.formatted(currency, key)))
                    .build(), HttpResponse.BodyHandlers.ofString()).body();
            return objectMapper.readTree(body);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
