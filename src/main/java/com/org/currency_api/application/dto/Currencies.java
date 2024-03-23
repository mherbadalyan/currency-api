package com.org.currency_api.application.dto;

import java.util.Set;

public record Currencies(Set<String> availableCurrencies) {
}
