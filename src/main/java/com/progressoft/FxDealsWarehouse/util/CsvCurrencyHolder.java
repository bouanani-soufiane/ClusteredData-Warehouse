package com.progressoft.FxDealsWarehouse.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Component
public class CsvCurrencyHolder implements CurrencyHolder {
    private final Set<String> currencyCodes;

    public CsvCurrencyHolder ( FileReader fileReader ) {
        try {
            Path currencyFilePath = Paths.get(new ClassPathResource("currencies.csv").getURI());
            this.currencyCodes = fileReader.read(currencyFilePath);
            validateCurrencyCodes(this.currencyCodes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load currencies.csv file", e);
        }
    }

    @Override
    public boolean exists ( String code ) {
        return currencyCodes.contains(code);
    }

    private void validateCurrencyCodes ( Set<String> codes ) {
        if (codes == null) {
            throw new NullPointerException("Currency codes set is null");
        }
        if (codes.isEmpty()) {
            throw new IllegalArgumentException("Currency codes set is empty");
        }
    }
}
