package com.postech.adjt.data.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = "com.postech.adjt.data.repository.jpa")
public class TestRepositoryConfiguration {

}