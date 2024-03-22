package com.org.currencyapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


class CurrencyApiApplicationTests {

    @Test
    void contextLoads() throws IOException, InterruptedException {
        String uri = "https://api.currencybeacon.com/v1/latest?base=%s&api_key=wICGZgx12MSo82YCYEivTVGVh4SBPJZ6";
        String body = HttpClient.newBuilder().build().send(HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri.formatted("USD")))
                .build(), HttpResponse.BodyHandlers.ofString()).body();
        JsonNode jsonNode = new ObjectMapper().readTree(body);
        int x =1;
    }

}
