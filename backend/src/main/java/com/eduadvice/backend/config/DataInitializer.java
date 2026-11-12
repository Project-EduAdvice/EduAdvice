package com.eduadvice.backend.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    CommandLineRunner initDatabase(DataInitializerService initializerService) {
        return args -> initializerService.initialize();
    }
}
