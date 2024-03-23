package com.org.currency_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.currency_api.application.dto.CreateRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerIntegrationTest {

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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void testAddCurrency() throws Exception {
        CreateRequest request = new CreateRequest("EUR");
        mockMvc.perform(MockMvcRequestBuilders.post("/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.message").value("added"));
    }

    @Test
    @Order(2)
    void testGetCurrency() throws Exception {
        String currency = "EUR";
        mockMvc.perform(MockMvcRequestBuilders.get("/exchange-rates/{currency}", currency))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.updateDate").isNotEmpty())
                .andExpect(jsonPath("$.rates").isMap())
        ;
    }

    @Test
    @Order(3)
    void testGetCurrencyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/currencies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.availableCurrencies").isArray());
    }
}
