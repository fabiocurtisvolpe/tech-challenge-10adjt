package com.postech.adjt.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

        @Value("${app.version}")
        private String appVersion;

        @Value("${app.openapi.dev-url:http://localhost:8080}")
        private String devUrl;

        @Value("${app.openapi.prod-url:http://localhost:8080}")
        private String prodUrl;

        @Bean
        public OpenAPI adjtAPI() {
                Server devServer = new Server();
                devServer.setUrl(devUrl);
                devServer.setDescription("URL do servidor em desenvolvimento");

                Server prodServer = new Server();
                prodServer.setUrl(prodUrl);
                prodServer.setDescription("URL do servidor em produção");

                Contact contact = new Contact();
                contact.setEmail("curtis.fabio@gmail.com");
                contact.setName("Fabio Curtis Volpe");

                License mitLicense = new License()
                                .name("GNU License")
                                .url("https://github.com/fabiocurtisvolpe/tech-challenge-10adjt/blob/main/LICENSE");

                Info info = new Info()
                                .title("API do tech-challenge-10adjt")
                                .version(appVersion)
                                .contact(contact)
                                .description("Esta API expõe endpoints para gerenciar o meu tech-challenge-10adjt")
                                .termsOfService("https://github.com/fabiocurtisvolpe/tech-challenge-10adjt")
                                .license(mitLicense);

                return new OpenAPI()
                                .info(info)
                                .servers(List.of(devServer, prodServer));
        }
}