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
    private final String uri;
    private final String key;

    public ExternalResourceImpl(ObjectMapper objectMapper, HttpClient httpClient,
                                @Value(value = "${web.api.uri}")
                                String uri,
                                @Value(value = "${web.api.uri}")
                                String key) {
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
        this.uri = uri;
        this.key = key;
    }


    @Override
    public ExchangeRateDto getExchangeRateUpdate(String currency) {
        var jsonNode = doGet(currency);
        return toDto(jsonNode);
    }

    private JsonNode doGet(String currency) {
        try {
            HttpResponse<String> send = httpClient.send(HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(uri.formatted(currency, key)))
                    .build(), HttpResponse.BodyHandlers.ofString());
            var body = send.body();
            return objectMapper.readTree(body);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
