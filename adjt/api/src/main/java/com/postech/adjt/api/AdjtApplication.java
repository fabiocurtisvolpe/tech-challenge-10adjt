package com.postech.adjt.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EntityScan({
        "com.postech.adjt.domain.model",
        "com.postech.adjt.data.entidade"
})
@EnableJpaRepositories("com.postech.adjt.data.repository")
@ComponentScan({
        "com.postech.adjt.api",
        "com.postech.adjt.domain",
        "com.postech.adjt.data"
})
public class AdjtApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdjtApplication.class, args);
    }
}
