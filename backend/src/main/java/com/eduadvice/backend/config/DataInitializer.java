package com.eduadvice.backend.config;

import com.eduadvice.backend.entity.Parcours;
import com.eduadvice.backend.entity.Semester;
import com.eduadvice.backend.repository.ParcoursRepository;
import com.eduadvice.backend.repository.SemesterRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            ParcoursRepository parcoursRepository,
            SemesterRepository semesterRepository) {

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

            if (semesterRepository.count() == 0) {

                Parcours ei2d = parcoursRepository.findAll().stream()
                        .filter(p -> p.getName().equals("EI2D"))
                        .findFirst()
                        .orElseThrow();

                Parcours p2s = parcoursRepository.findAll().stream()
                        .filter(p -> p.getName().equals("P2S"))
                        .findFirst()
                        .orElseThrow();

                List<Semester> semesters = List.of(
                    new Semester(3, ei2d),
                    new Semester(4, ei2d),
                    new Semester(3, p2s),
                    new Semester(4, p2s)
                );

                semesterRepository.saveAll(semesters);
            }
        };
    }
}