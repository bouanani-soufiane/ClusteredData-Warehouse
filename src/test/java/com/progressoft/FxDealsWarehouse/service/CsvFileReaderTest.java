package com.progressoft.FxDealsWarehouse.service;

import com.progressoft.FxDealsWarehouse.currencystore.CsvFileReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CsvFileReaderTest {

    private final CsvFileReader csvFileReader = new CsvFileReader();

    @Test
    void readCurrenciesCsvFileShouldReturnExpectedCurrencyCodes ( @TempDir Path tempDir ) throws IOException {
        Path csvFile = tempDir.resolve("currencies.csv");
        List<String> lines = List.of("CURRENCIES :", "USD,", "EUR,", "GBP,");
        Files.write(csvFile, lines);

        Set<String> actualCodes = csvFileReader.read(csvFile);
        Set<String> expectedCodes = Set.of("USD", "EUR", "GBP");

        assertEquals(expectedCodes, actualCodes);
    }

    @Test
    void readNonexistentFileShouldThrowRuntimeException () {
        Path nonExistingFile = Path.of("nonexistent.csv");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> csvFileReader.read(nonExistingFile));
        assertTrue(exception.getMessage().contains("Failed to read currency codes file"));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof IOException);
    }
}
