
package com.eduadvice.backend.config;

import com.eduadvice.backend.entity.Parcours;
import com.eduadvice.backend.entity.Semester;
import com.eduadvice.backend.entity.TeachingUnit;
import com.eduadvice.backend.entity.TeachingUnitCategory;

import com.eduadvice.backend.repository.ParcoursRepository;
import com.eduadvice.backend.repository.SemesterRepository;
import com.eduadvice.backend.repository.TeachingUnitRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            ParcoursRepository parcoursRepository,
            SemesterRepository semesterRepository,
            TeachingUnitRepository teachingUnitRepository) {

        return args -> {

            // Initialize parcours
            Parcours ei2d = getOrCreateParcours(
                    parcoursRepository,
                    "EI2D",
                    "Exploration Informatique des Données et Décisionnel."
            );

            Parcours p2s = getOrCreateParcours(
                    parcoursRepository,
                    "P2S",
                    "Programmation, Sûreté et Sécurité."
            );

            // Initialize semesters
            Semester ei2dS3 = getOrCreateSemester(
                    semesterRepository, ei2d, 3
            );

            Semester ei2dS4 = getOrCreateSemester(
                    semesterRepository, ei2d, 4
            );

            Semester p2sS3 = getOrCreateSemester(
                    semesterRepository, p2s, 3
            );

            Semester p2sS4 = getOrCreateSemester(
                    semesterRepository, p2s, 4
            );

            // Teaching Units
            List<TeachingUnit> teachingUnits = List.of(

                    // P2S — Semester 3

                    new TeachingUnit(
                            p2sS3,
                            "Majeure I : Programmation",
                            TeachingUnitCategory.MAJOR_1,
                            new BigDecimal("12")
                    ),

                    new TeachingUnit(
                            p2sS3,
                            "Majeure II : Spécification, Sûreté et Sécurité",
                            TeachingUnitCategory.MAJOR_2,
                            new BigDecimal("12")
                    ),

                    new TeachingUnit(
                            p2sS3,
                            "UE transversales",
                            TeachingUnitCategory.TRANSVERSAL,
                            new BigDecimal("6")
                    ),

                    new TeachingUnit(
                            p2sS3,
                            "UE spécifique parcours EUR",
                            TeachingUnitCategory.TRANSVERSAL,
                            null
                    ),

                    // P2S — Semester 4

                    new TeachingUnit(
                            p2sS4,
                            "Mineures",
                            TeachingUnitCategory.MINOR,
                            new BigDecimal("6")
                    ),

                    new TeachingUnit(
                            p2sS4,
                            "Professionnalisation",
                            TeachingUnitCategory.PROFESSIONALIZATION,
                            new BigDecimal("24")
                    ),

                    // EI2D — Semester 3

                    new TeachingUnit(
                            ei2dS3,
                            "Majeure (I) : Apprentissage Artificiel et Science des Données",
                            TeachingUnitCategory.MAJOR_1,
                            new BigDecimal("12")
                    ),

                    new TeachingUnit(
                            ei2dS3,
                            "Majeure (II) : Ingénierie des Données et Informatique Décisionnelle",
                            TeachingUnitCategory.MAJOR_2,
                            new BigDecimal("12")
                    ),

                    new TeachingUnit(
                            ei2dS3,
                            "UE Transversales",
                            TeachingUnitCategory.TRANSVERSAL,
                            new BigDecimal("6")
                    ),

                    new TeachingUnit(
                            ei2dS3,
                            "UE spécifique parcours EUR",
                            TeachingUnitCategory.TRANSVERSAL,
                            null
                    ),

                    new TeachingUnit(
                            ei2dS3,
                            "UE Facultatives",
                            TeachingUnitCategory.OPTIONAL,
                            null
                    ),

                    // EI2D — Semester 4

                    new TeachingUnit(
                            ei2dS4,
                            "Mineures",
                            TeachingUnitCategory.MINOR,
                            new BigDecimal("6")
                    ),

                    new TeachingUnit(
                            ei2dS4,
                            "Professionnalisation",
                            TeachingUnitCategory.PROFESSIONALIZATION,
                            new BigDecimal("24")
                    )
            );

            // Seed each semester independently
            for (Semester semester : List.of(
                    p2sS3,
                    p2sS4,
                    ei2dS3,
                    ei2dS4
            )) {

                if (teachingUnitRepository
                        .findBySemesterId(semester.getId())
                        .isEmpty()) {

                    List<TeachingUnit> semesterUnits = teachingUnits.stream()
                            .filter(unit -> unit.getSemester().getId()
                                    .equals(semester.getId()))
                            .toList();

                    teachingUnitRepository.saveAll(semesterUnits);
                }
            }
        };
    }

    private Parcours getOrCreateParcours(
            ParcoursRepository repository,
            String name,
            String description) {

        return repository.findAll().stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseGet(() -> repository.save(
                        new Parcours(
                                name,
                                "Master 2",
                                "Master Informatique",
                                "Informatique",
                                "Université Sorbonne Paris Nord",
                                description
                        )
                ));
    }

    private Semester getOrCreateSemester(
            SemesterRepository repository,
            Parcours parcours,
            int number) {

        return repository.findAll().stream()
                .filter(s -> s.getParcours().getId()
                        .equals(parcours.getId()))
                .filter(s -> s.getNumber() == number)
                .findFirst()
                .orElseGet(() -> repository.save(
                        new Semester(number, parcours)
                ));
    }
}
