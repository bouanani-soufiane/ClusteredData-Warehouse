package com.progressoft.FxDealsWarehouse.config;

import com.progressoft.FxDealsWarehouse.util.CsvCurrencyHolder;
import com.progressoft.FxDealsWarehouse.util.FileReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CurrencyConfig {

    @Bean
    public CsvCurrencyHolder currencyHolder ( FileReader fileReader ) {
        return new CsvCurrencyHolder(fileReader);
    }
}