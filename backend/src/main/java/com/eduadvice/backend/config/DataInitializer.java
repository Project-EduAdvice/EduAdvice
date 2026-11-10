
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

            // Initialize Parcours
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

            // Initialize Semesters
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

            // Retrieve semesters
            List<Semester> semesters = semesterRepository.findAll();

            Semester ei2dSemester3 = semesters.stream()
                    .filter(s -> s.getParcours().getName().equals("EI2D"))
                    .filter(s -> s.getNumber() == 3)
                    .findFirst()
                    .orElseThrow();

            Semester ei2dSemester4 = semesters.stream()
                    .filter(s -> s.getParcours().getName().equals("EI2D"))
                    .filter(s -> s.getNumber() == 4)
                    .findFirst()
                    .orElseThrow();

            Semester p2sSemester3 = semesters.stream()
                    .filter(s -> s.getParcours().getName().equals("P2S"))
                    .filter(s -> s.getNumber() == 3)
                    .findFirst()
                    .orElseThrow();

            Semester p2sSemester4 = semesters.stream()
                    .filter(s -> s.getParcours().getName().equals("P2S"))
                    .filter(s -> s.getNumber() == 4)
                    .findFirst()
                    .orElseThrow();

            // Teaching Units
            List<TeachingUnit> teachingUnits = List.of(

                // P2S — SEMESTER 3

                // Major I — Programmation
                new TeachingUnit(p2sSemester3, "G5PLSDJA",
                        "Développement JavaScript avancé et ubiquitaire",
                        TeachingUnitCategory.MAJOR_1, new BigDecimal("3")),

                new TeachingUnit(p2sSemester3, "G5PRLSED",
                        "Environnements de développement libres",
                        TeachingUnitCategory.MAJOR_1, new BigDecimal("3")),

                new TeachingUnit(p2sSemester3, "G5P2SML",
                        "Méthodologies et langages avancés",
                        TeachingUnitCategory.MAJOR_1, new BigDecimal("3")),

                new TeachingUnit(p2sSemester3, "G5PRLSPM",
                        "Programmation modulaire et résiliente",
                        TeachingUnitCategory.MAJOR_1, new BigDecimal("3")),

                // Major II — Spécification, Sûreté et Sécurité
                new TeachingUnit(p2sSemester3, "G5PRLSSP",
                        "Programmes et preuves",
                        TeachingUnitCategory.MAJOR_2, new BigDecimal("3")),

                new TeachingUnit(p2sSemester3, "G5PLS2RP",
                        "Réseaux de Petri de haut niveau",
                        TeachingUnitCategory.MAJOR_2, new BigDecimal("3")),

                new TeachingUnit(p2sSemester3, "G5P2SSV",
                        "Spécification et vérification de systèmes complexes",
                        TeachingUnitCategory.MAJOR_2, new BigDecimal("3")),

                new TeachingUnit(p2sSemester3, "G5PRLSSS",
                        "Sûreté et sécurité",
                        TeachingUnitCategory.MAJOR_2, new BigDecimal("3")),

                // Transversal
                new TeachingUnit(p2sSemester3, "GANGMS3",
                        "Anglais",
                        TeachingUnitCategory.TRANSVERSAL, new BigDecimal("2")),

                new TeachingUnit(p2sSemester3, "G5INFEA",
                        "Éthique des algorithmes",
                        TeachingUnitCategory.TRANSVERSAL, new BigDecimal("1")),

                new TeachingUnit(p2sSemester3, "G5INFSSA",
                        "Soft Skills",
                        TeachingUnitCategory.TRANSVERSAL, new BigDecimal("1")),

                new TeachingUnit(p2sSemester3, "G5TEC3",
                        "TEC",
                        TeachingUnitCategory.TRANSVERSAL, new BigDecimal("2")),

                // EUR-specific
                new TeachingUnit(p2sSemester3, null,
                        "Combinatoire et probabilités",
                        TeachingUnitCategory.TRANSVERSAL, new BigDecimal("6")),

                new TeachingUnit(p2sSemester3, null,
                        "Le petit séminaire de combinatoire et probabilités",
                        TeachingUnitCategory.TRANSVERSAL, new BigDecimal("3")),


                // P2S — SEMESTER 4

                // P2S minors
                new TeachingUnit(p2sSemester4, "G5INFCOS",
                        "Cartographie et OpenStreetMap",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(p2sSemester4, "G5INFDIT",
                        "Détection d'intrusions",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(p2sSemester4, "G5INFMCE",
                        "Maîtrise des crises dans les environnements critiques",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(p2sSemester4, "G5INFMS",
                        "Monitoring de systèmes cyber-physiques",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                // Common minors
                new TeachingUnit(p2sSemester4, "G5INFARI",
                        "Analyse des réseaux d'interactions",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(p2sSemester4, "G5INFAM",
                        "Apprentissage et Metaapprentissage appliqués à l'IoT",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(p2sSemester4, "G5INFCY",
                        "Cybersécurité & IA",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(p2sSemester4, "G5INFDEU",
                        "Design et expérience utilisateur",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(p2sSemester4, "G5INFIQ",
                        "Informatique quantique",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                // Internship
                new TeachingUnit(p2sSemester4, "G5P2SS",
                        "Stage Recherche/Développement",
                        TeachingUnitCategory.MAJOR_1, new BigDecimal("24")),


                // EI2D — SEMESTER 3

                // Major I — Apprentissage Artificiel et Science des Données
                new TeachingUnit(ei2dSemester3, "G5EI2DAS",
                        "Apprentissage statistique",
                        TeachingUnitCategory.MAJOR_1, new BigDecimal("3")),

                new TeachingUnit(ei2dSemester3, "G5EI2DDL",
                        "Deep Learning",
                        TeachingUnitCategory.MAJOR_1, new BigDecimal("3")),

                new TeachingUnit(ei2dSemester3, "G5EI2DSD",
                        "Science des données numériques",
                        TeachingUnitCategory.MAJOR_1, new BigDecimal("3")),

                new TeachingUnit(ei2dSemester3, "G5EI2DSE",
                        "Statistique exploratoire multidimensionnelle",
                        TeachingUnitCategory.MAJOR_1, new BigDecimal("3")),

                // Major II — Ingénierie des Données et Informatique Décisionnelle
                new TeachingUnit(ei2dSemester3, "G5EI2DBD",
                        "Bases de données avancées",
                        TeachingUnitCategory.MAJOR_2, new BigDecimal("3")),

                new TeachingUnit(ei2dSemester3, "G5EI2DM",
                        "Data Mining",
                        TeachingUnitCategory.MAJOR_2, new BigDecimal("3")),

                new TeachingUnit(ei2dSemester3, "G5EI2DDW",
                        "Data Warehouse",
                        TeachingUnitCategory.MAJOR_2, new BigDecimal("3")),

                new TeachingUnit(ei2dSemester3, "G5EI2ID",
                        "Informatique Décisionnelle",
                        TeachingUnitCategory.MAJOR_2, new BigDecimal("3")),

                // Transversal
                new TeachingUnit(ei2dSemester3, "GANGMS3",
                        "Anglais",
                        TeachingUnitCategory.TRANSVERSAL, new BigDecimal("2")),

                new TeachingUnit(ei2dSemester3, "G5INFEA",
                        "Éthique des algorithmes",
                        TeachingUnitCategory.TRANSVERSAL, new BigDecimal("1")),

                new TeachingUnit(ei2dSemester3, "G5INFSSA",
                        "Soft Skills",
                        TeachingUnitCategory.TRANSVERSAL, new BigDecimal("1")),

                new TeachingUnit(ei2dSemester3, "G5TEC3",
                        "TEC",
                        TeachingUnitCategory.TRANSVERSAL, new BigDecimal("2")),

                // EUR-specific
                new TeachingUnit(ei2dSemester3, null,
                        "Combinatoire et probabilités",
                        TeachingUnitCategory.TRANSVERSAL, new BigDecimal("6")),

                new TeachingUnit(ei2dSemester3, null,
                        "Le petit séminaire de combinatoire et probabilités",
                        TeachingUnitCategory.TRANSVERSAL, new BigDecimal("3")),


                // EI2D — SEMESTER 4

                // EI2D minors
                new TeachingUnit(ei2dSemester4, "G5INFARV",
                        "Apprentissage de représentations visuelles",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(ei2dSemester4, null,
                        "Décompositions tensorielles en grande dimension",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(ei2dSemester4, "G5INFDTX",
                        "Données textuelles et modèles génératifs",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(ei2dSemester4, "G5INFEIA",
                        "Explicabilité et Intelligence Artificielle",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                // Common minors
                new TeachingUnit(ei2dSemester4, "G5INFARI",
                        "Analyse des réseaux d'interactions",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(ei2dSemester4, "G5INFAM",
                        "Apprentissage et Metaapprentissage appliqués à l'IoT",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(ei2dSemester4, "G5INFCY",
                        "Cybersécurité & IA",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(ei2dSemester4, "G5INFDEU",
                        "Design et expérience utilisateur",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                new TeachingUnit(ei2dSemester4, "G5INFIQ",
                        "Informatique quantique",
                        TeachingUnitCategory.MINOR, new BigDecimal("3")),

                // Internship
                new TeachingUnit(ei2dSemester4, "G5EI2DS",
                        "Stage Recherche/Développement",
                        TeachingUnitCategory.MAJOR_1, new BigDecimal("24"))
            );

            // Seed each semester independently
            for (Semester semester : List.of(
                    p2sSemester3,
                    p2sSemester4,
                    ei2dSemester3,
                    ei2dSemester4
            )) {
                if (teachingUnitRepository.findBySemesterId(semester.getId()).isEmpty()) {

                    List<TeachingUnit> semesterUnits = teachingUnits.stream()
                            .filter(unit -> unit.getSemester().getId().equals(semester.getId()))
                            .toList();

                    teachingUnitRepository.saveAll(semesterUnits);
                }
            }
        };
    }
}
