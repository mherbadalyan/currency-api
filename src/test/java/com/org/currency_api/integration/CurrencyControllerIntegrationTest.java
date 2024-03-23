package com.org.currency_api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.currency_api.application.dto.CreateRequest;
import com.org.currency_api.domain.repository.ExchangeRateEntityRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CurrencyControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ExchangeRateEntityRepository repository;

    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.2-alpine")
            .withDatabaseName("tests")
            .withUsername("root")
            .withPassword("root");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    static void start() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void stop() {
        postgreSQLContainer.stop();
    }

    @AfterEach
    void cleanRepo() {
        repository.deleteAll();
    }

    @Test
    void testGetCurrency() throws Exception {
        var currency = "EUR";
        var request = new CreateRequest(currency);
        mockMvc.perform(MockMvcRequestBuilders.post("/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.updateDate").isNotEmpty())
                .andExpect(jsonPath("$.rates").isMap());


        mockMvc.perform(MockMvcRequestBuilders.get("/currencies/{currency}/exchange-rates", currency))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.updateDate").isNotEmpty())
                .andExpect(jsonPath("$.rates").isMap());
    }

    @Test
    void testGetCurrencyList() throws Exception {
        var currency = "EUR";
        var request = new CreateRequest(currency);
        mockMvc.perform(MockMvcRequestBuilders.post("/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.updateDate").isNotEmpty())
                .andExpect(jsonPath("$.rates").isMap());

        mockMvc.perform(MockMvcRequestBuilders.get("/currencies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.availableCurrencies").isArray());
    }
}
