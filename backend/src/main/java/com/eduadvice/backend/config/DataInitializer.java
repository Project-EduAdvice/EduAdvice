
package com.eduadvice.backend.config;

import com.eduadvice.backend.config.DataInitializerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(com.eduadvice.backend.config.DataInitializerService initializerService) {
        return args -> initializerService.initialize();
    }
}
