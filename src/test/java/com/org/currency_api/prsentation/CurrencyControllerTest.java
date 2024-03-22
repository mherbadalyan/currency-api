package com.org.currency_api.prsentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.currency_api.application.dto.CreateRequest;
import com.org.currency_api.application.dto.Currencies;
import com.org.currency_api.application.dto.ExchangeRateDto;
import com.org.currency_api.infrastructure.service.CurrencyService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CurrencyController.class)
@AutoConfigureMockMvc
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Autowired
    private ObjectMapper objectMapper;

    EasyRandom generator;

    @BeforeEach
    void init() {
        generator = new EasyRandom();
    }

    @Test
    void testGetList() throws Exception {
        when(currencyService.getCurrencies()).thenReturn(new Currencies(Set.of()));
        mockMvc.perform(MockMvcRequestBuilders.get("/currencies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetCurrency() throws Exception {
        var currency = generator.nextObject(String.class);
        var exchangeRateDto = ExchangeRateDto.builder().currency(currency).build();
        when(currencyService.getExchangeRate(currency)).thenReturn(exchangeRateDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/exchange-rates/%s".formatted(currency)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testAddCurrency() throws Exception {
        var currency = generator.nextObject(String.class);
        var request = new CreateRequest(currency);
        var requestBody = objectMapper.writeValueAsString(request);
        when(currencyService.addCurrency(currency)).thenReturn(currency);
        mockMvc.perform(MockMvcRequestBuilders.post("/currencies")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}