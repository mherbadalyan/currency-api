package com.org.currency_api.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.http.HttpClient;

@Configuration
@EnableScheduling
public class Config {
    @Bean
    HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }
}
