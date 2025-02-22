package com.progressoft.FxDealsWarehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FxDealsWarehouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxDealsWarehouseApplication.class, args);
	}

}
