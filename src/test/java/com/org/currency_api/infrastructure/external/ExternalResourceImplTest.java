package com.org.currency_api.infrastructure.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExternalResourceImplTest {

    EasyRandom generator;

    @BeforeEach
    void init() {
        generator = new EasyRandom();
    }

    @Test
    void testDoGet() throws IOException, InterruptedException {
        //when
        var mockResponseBody = generator.nextObject(String.class);
        var mockUri = "https://api.example.com/rates";
        var mockKey = generator.nextObject(String.class);
        var mockObjectMapper = Mockito.mock(ObjectMapper.class);
        var mockResponse = Mockito.mock(HttpResponse.class);
        var httpClient = Mockito.mock(HttpClient.class);
        var jsonNode = Mockito.mock(JsonNode.class);


        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass()))).thenReturn(mockResponse);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockObjectMapper.readTree(any(String.class))).thenReturn(jsonNode);
        var externalResource = new ExternalResourceImpl(mockObjectMapper, httpClient, mockUri, mockKey);

        var response = externalResource.doGet(generator.nextObject(String.class));

        // then
        verify(httpClient).send(any(HttpRequest.class), any());
    }

}
