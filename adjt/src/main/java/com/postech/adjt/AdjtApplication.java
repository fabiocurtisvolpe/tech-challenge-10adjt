package com.postech.adjt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EntityScan("com.postech.adjt")
@EnableJpaRepositories("com.postech.adjt.repository")
@ComponentScan("com.postech.adjt")
public class AdjtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdjtApplication.class, args);
	}

}
