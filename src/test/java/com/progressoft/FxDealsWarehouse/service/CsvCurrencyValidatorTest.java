package com.progressoft.FxDealsWarehouse.service;

import com.progressoft.FxDealsWarehouse.currencystore.CurrencyHolder;
import com.progressoft.FxDealsWarehouse.exception.CurrencyNotAvailableException;
import com.progressoft.FxDealsWarehouse.exception.InvalidCurrencyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class CsvCurrencyValidatorTest {


    @Mock
    private CurrencyHolder currencyHolder;

    @InjectMocks
    private CsvCurrencyValidator csvCurrencyValidator;


    @BeforeEach
    void setUp () {
        when(currencyHolder.exists("USD")).thenReturn(true);
        when(currencyHolder.exists("EUR")).thenReturn(true);
        when(currencyHolder.exists("XYZ")).thenReturn(false);
    }

    @Test
    void shouldPassValidationForValidCurrencies () {
        assertDoesNotThrow(
                () -> csvCurrencyValidator.validateCurrencyExchange("USD", "EUR")
        );
    }

    @Test
    void shouldThrowInvalidCurrencyExceptionForInvalidPattern () {
        assertThrows(InvalidCurrencyException.class,
                () -> csvCurrencyValidator.validateCurrencyExchange("US", "EUR")
        );

        assertThrows(InvalidCurrencyException.class,
                () -> csvCurrencyValidator.validateCurrencyExchange("USD", "EU")
        );
    }

    @Test
    void shouldThrowCurrencyNotAvailableExceptionForNonExistentCurrency () {
        assertThrows(CurrencyNotAvailableException.class,
                () -> csvCurrencyValidator.validateCurrencyExchange("XYZ", "EUR")
        );

        assertThrows(CurrencyNotAvailableException.class,
                () -> csvCurrencyValidator.validateCurrencyExchange("USD", "XYZ")
        );
    }

    @Test
    void shouldThrowInvalidCurrencyExceptionForSameCurrencies () {
        assertThrows(InvalidCurrencyException.class,
                () -> csvCurrencyValidator.validateCurrencyExchange("USD", "USD")
        );
    }

}
