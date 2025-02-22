package com.progressoft.FxDealsWarehouse.service;

import com.progressoft.FxDealsWarehouse.currencystore.CurrencyHolder;
import com.progressoft.FxDealsWarehouse.exception.CurrencyNotAvailableException;
import com.progressoft.FxDealsWarehouse.exception.InvalidCurrencyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CsvCurrencyValidator implements CurrencyValidator {
    private final CurrencyHolder currencyHolder;

    private final String CURRENCYPATTERN = "^[A-Z]{3}$";

    @Override
    public void validateCurrencyExchange ( String fromCurrency, String toCurrency ) {
        checkPattern(fromCurrency, toCurrency);
        checkCurrencyExistence(toCurrency);
        checkCurrencyExistence(fromCurrency);
        validateCurrencyDifferentiation(fromCurrency, toCurrency);
    }


    private void validateCurrencyDifferentiation ( String fromCurrency, String toCurrency ) {
        if (fromCurrency.equals(toCurrency)) {
            String errorMessage = String.format("Invalid deal: fromCurrency [%s] and toCurrency [%s] cannot be the same.",
                    fromCurrency, toCurrency);
            throw new InvalidCurrencyException(errorMessage);
        }
    }


    private void checkCurrencyExistence ( String value ) {

        if (!currencyHolder.exists(value)) {
            String errorMessage = String.format("Currency not available: %s", value);
            throw new CurrencyNotAvailableException(errorMessage);
        }
    }


    private void checkPattern ( String fromCurrency, String toCurrency ) {

        if (!isMatches(fromCurrency)) {
            throw new InvalidCurrencyException("Invalid from currency code format");
        }
        if (!isMatches(toCurrency)) {
            throw new InvalidCurrencyException("Invalid to currency code format");
        }
    }

    private boolean isMatches ( String fromCurrency ) {
        return fromCurrency.matches(CURRENCYPATTERN);
    }
}
