package com.progressoft.FxDealsWarehouse.service;

import com.progressoft.FxDealsWarehouse.currencystore.CsvCurrencyHolder;
import com.progressoft.FxDealsWarehouse.currencystore.CurrencyHolder;
import com.progressoft.FxDealsWarehouse.currencystore.FileReader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CsvCurrencyHolderTest {

    @Mock
    private FileReader fileReader;

    @Nested
    class InitializationTests {

        @Test
        void initializeWithValidCurrencyCodesShouldSucceed() throws IOException {
            Set<String> currencyCodes = Set.of("USD", "EUR", "GBP");
            when(fileReader.read(any(Path.class))).thenReturn(currencyCodes);

            CurrencyHolder currencyHolder = new CsvCurrencyHolder(fileReader);

            assertTrue(currencyHolder.exists("USD"));
            assertTrue(currencyHolder.exists("EUR"));
            assertTrue(currencyHolder.exists("GBP"));
            assertFalse(currencyHolder.exists("XYZ"));
        }

        @Test
        void initializeWithNullCurrencyCodesShouldThrowNullPointerException() throws IOException {
            when(fileReader.read(any(Path.class))).thenReturn(null);

            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> new CsvCurrencyHolder(fileReader));
            assertEquals("Currency codes set is null", exception.getMessage());
        }

        @Test
        void initializeWithEmptyCurrencyCodesShouldThrowIllegalArgumentException() throws IOException {
            when(fileReader.read(any(Path.class))).thenReturn(Set.of());

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new CsvCurrencyHolder(fileReader));
            assertEquals("Currency codes set is empty", exception.getMessage());
        }

        @Test
        void initializeWhenIOExceptionOccursShouldThrowRuntimeException() {
            IOException ioException = new IOException("Test IOException");
            when(fileReader.read(any(Path.class))).thenAnswer(invocation -> {
                throw sneakyThrow(ioException);
            });

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> new CsvCurrencyHolder(fileReader));
            assertTrue(exception.getMessage().contains("Failed to load currencies.csv file"));
            assertEquals(ioException, exception.getCause());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> RuntimeException sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}
