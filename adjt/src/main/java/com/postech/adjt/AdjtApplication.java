package com.postech.adjt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AdjtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdjtApplication.class, args);
	}

}
