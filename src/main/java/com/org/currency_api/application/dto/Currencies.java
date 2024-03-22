package com.org.currency_api.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Currencies {
    private Set<String> availableCurrencies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currencies that = (Currencies) o;
        return Objects.equals(availableCurrencies, that.availableCurrencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(availableCurrencies);
    }
}
