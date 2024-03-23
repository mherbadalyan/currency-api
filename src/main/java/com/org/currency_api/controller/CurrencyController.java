package com.org.currency_api.controller;

import com.org.currency_api.application.dto.CreateRequest;
import com.org.currency_api.application.dto.Currencies;
import com.org.currency_api.application.dto.ExchangeRateDto;
import com.org.currency_api.infrastructure.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Currency Exchange Service", description = """
        The Currency Exchange Service API provides functionalities
        for managing currencies and accessing exchange rates.
        """)
@RequestMapping("/currencies")
public class CurrencyController {
    private final CurrencyService service;

    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    @Operation(summary = "Get currency list",
            description = "Retrieves a list of available currencies supported by the Currency Exchange Service.")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Currencies getList() {
        return service.getCurrencies();
    }

    @Operation(summary = "Get exchange rate",
            description = "Retrieves exchange rates for a specific currency.")
    @GetMapping("/{currency}/exchange-rates")
    @ResponseStatus(HttpStatus.OK)
    public ExchangeRateDto getCurrency(@PathVariable("currency") String currency) {
        return service.getExchangeRate(currency);
    }

    @Operation(summary = "Add currency",
            description = "Adds a new currency to the Currency Exchange Service. ")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ExchangeRateDto addCurrency(@RequestBody CreateRequest request) {
        return service.addCurrency(request.currency());
    }
}
