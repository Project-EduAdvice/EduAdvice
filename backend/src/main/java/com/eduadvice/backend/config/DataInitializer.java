package com.eduadvice.backend.config;
import com.eduadvice.backend.entity.Parcours;
import com.eduadvice.backend.repository.ParcoursRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(ParcoursRepository parcoursRepository) {
        return args -> {
            if (parcoursRepository.count() == 0) {
                List<Parcours> parcours = List.of(
                    new Parcours(
                            "EI2D",
                            "Master 2",
                            "Master Informatique",
                            "Informatique",
                            "Université Sorbonne Paris Nord",
                            "Exploration Informatique des Données et Décisionnel."
                    ),
                    new Parcours(
                            "P2S",
                            "Master 2",
                            "Master Informatique",
                            "Informatique",
                            "Université Sorbonne Paris Nord",
                            "Programmation, Sûreté et Sécurité."
                    )
                );

                parcoursRepository.saveAll(parcours);
            }
        };
    }
}