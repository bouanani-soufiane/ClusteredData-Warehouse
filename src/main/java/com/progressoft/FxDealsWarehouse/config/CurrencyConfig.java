package com.progressoft.FxDealsWarehouse.config;

import com.progressoft.FxDealsWarehouse.currencystore.CsvCurrencyHolder;
import com.progressoft.FxDealsWarehouse.currencystore.FileReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CurrencyConfig {

    @Bean
    public CsvCurrencyHolder currencyHolder ( FileReader fileReader ) {
        return new CsvCurrencyHolder(fileReader);
    }
}