package com.dmdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;

@SpringBootApplication(exclude = LiquibaseAutoConfiguration.class)
public class CarRentApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarRentApplication.class, args);
    }
}