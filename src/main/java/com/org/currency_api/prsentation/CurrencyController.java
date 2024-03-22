package com.org.currency_api.prsentation;

import com.org.currency_api.application.dto.CreateRequest;
import com.org.currency_api.application.dto.CreateResponse;
import com.org.currency_api.infrastructure.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Currency Exchange Service", description = """
        The Currency Exchange Service API provides functionalities
        for managing currencies and accessing exchange rates.
        """)
public class CurrencyController {
    private final CurrencyService service;

    public CurrencyController(CurrencyService service) {
        this.service = service;
    }


    @Operation(summary = "Get currency list",
            description = "Retrieves a list of available currencies supported by the Currency Exchange Service.")
    @GetMapping("/currencies")
    public ResponseEntity<?> getList() {
        var currencyList = service.getCurrencyList();
        return new ResponseEntity<>(currencyList, HttpStatus.OK);
    }

    @Operation(summary = "Get exchange rate",
            description = "Retrieves exchange rates for a specific currency.")
    @GetMapping("/exchange-rates/{currency}")
    public ResponseEntity<?> getCurrency(@PathVariable("currency") String currency) {
        var exchangeRate = service.getExchangeRate(currency);
        return new ResponseEntity<>(exchangeRate, HttpStatus.OK);
    }

    @Operation(summary = "Add currency",
            description = "Adds a new currency to the Currency Exchange Service. ")
    @PostMapping("/currencies")
    public ResponseEntity<?> addCurrency(@RequestBody CreateRequest request) {
        var savedCurrency = service.addCurrency(request.currency());
        var response = new CreateResponse(savedCurrency, "added");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
