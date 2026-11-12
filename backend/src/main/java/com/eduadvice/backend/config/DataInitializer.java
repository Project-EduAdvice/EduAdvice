
package com.eduadvice.backend.config;

import com.eduadvice.backend.entity.Parcours;
import com.eduadvice.backend.entity.Semester;
import com.eduadvice.backend.entity.TeachingUnit;
import com.eduadvice.backend.entity.TeachingUnitCategory;
import com.eduadvice.backend.entity.Subject;

import com.eduadvice.backend.repository.ParcoursRepository;
import com.eduadvice.backend.repository.SemesterRepository;
import com.eduadvice.backend.repository.TeachingUnitRepository;
import com.eduadvice.backend.repository.SubjectRepository;

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
            TeachingUnitRepository teachingUnitRepository,
            SubjectRepository subjectRepository) {

        return args -> {

            // ==========================================
            // PARCOURS
            // ==========================================

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

            // ==========================================
            // SEMESTERS
            // ==========================================

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

            // ==========================================
            // TEACHING UNITS
            // ==========================================

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

            // Insert Teaching Units if a semester has none
            for (Semester semester : List.of(
                    p2sS3, p2sS4, ei2dS3, ei2dS4
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

            // ==========================================
            // SUBJECTS
            // ==========================================

            seedSubjects(
                    subjectRepository,
                    teachingUnitRepository,
                    p2sS3,
                    p2sS4,
                    ei2dS3,
                    ei2dS4
            );
        };
    }

    // ==========================================
    // SUBJECT INITIALIZATION
    // ==========================================

    private void seedSubjects(
            SubjectRepository subjectRepository,
            TeachingUnitRepository teachingUnitRepository,
            Semester p2sS3,
            Semester p2sS4,
            Semester ei2dS3,
            Semester ei2dS4) {

        // ==========================================
        // P2S — SEMESTER 3
        // ==========================================

        TeachingUnit p2sProgramming = getTeachingUnit(
                teachingUnitRepository, p2sS3,
                "Majeure I : Programmation"
        );

        addSubjects(subjectRepository, p2sProgramming, List.of(

                subject("Développement JavaScript avancé et ubiquitaire", "3",
                        "Approfondissement du développement JavaScript pour concevoir des applications exécutées dans différents environnements.",
                        "JavaScript avancé, programmation asynchrone, APIs, environnements d'exécution et développement d'applications web et multiplateformes."),

                subject("Environnements de développements libres", "3",
                        "Découverte et utilisation des environnements et outils de développement fondés sur les logiciels libres.",
                        "Écosystème open source, outils de développement, systèmes Linux, gestion de versions, compilation et automatisation."),

                subject("Méthodologies et langages avancés", "3",
                        "Étude de méthodes et de langages permettant de concevoir des logiciels complexes de manière structurée.",
                        "Paradigmes de programmation, abstraction, conception logicielle, systèmes de types et utilisation de langages avancés."),

                subject("Programmation modulaire et résiliente", "3",
                        "Conception de logiciels organisés en composants indépendants et capables de mieux gérer les erreurs et les défaillances.",
                        "Modularité, interfaces, séparation des responsabilités, gestion des erreurs, tolérance aux pannes et robustesse logicielle.")
        ));

        TeachingUnit p2sSpecification = getTeachingUnit(
                teachingUnitRepository, p2sS3,
                "Majeure II : Spécification, Sûreté et Sécurité"
        );

        addSubjects(subjectRepository, p2sSpecification, List.of(

                subject("Programmes et preuves", "3",
                        "Étude des méthodes formelles permettant de raisonner sur la correction des programmes.",
                        "Logique, assertions, préconditions, postconditions, invariants, preuves de programmes et vérification de propriétés."),

                subject("Réseaux de Petri de haut niveau", "3",
                        "Modélisation et analyse de systèmes concurrents et distribués à l'aide de réseaux de Petri.",
                        "Places, transitions, jetons, réseaux colorés, synchronisation, concurrence, propriétés comportementales et analyse de modèles."),

                subject("Spécification et vérification de systèmes complexes", "3",
                        "Méthodes de description formelle et de vérification du comportement de systèmes informatiques complexes.",
                        "Spécification, modélisation, propriétés de systèmes, vérification formelle, model checking et détection d'incohérences."),

                subject("Sûreté et sécurité", "3",
                        "Étude des principes permettant de concevoir des systèmes fiables et de protéger leurs composants et leurs données.",
                        "Sûreté de fonctionnement, sécurité informatique, menaces, vulnérabilités, analyse des risques et mécanismes de protection.")
        ));

        TeachingUnit p2sTransversal = getTeachingUnit(
                teachingUnitRepository, p2sS3,
                "UE transversales"
        );

        addSubjects(subjectRepository, p2sTransversal, List.of(

                subject("Anglais", "2",
                        "Développement des compétences en anglais dans un contexte universitaire et professionnel.",
                        "Compréhension écrite et orale, expression, vocabulaire scientifique et technique et communication professionnelle."),

                subject("Éthique des algorithmes", "1",
                        "Réflexion sur les enjeux éthiques et sociétaux liés à la conception et à l'utilisation des algorithmes.",
                        "Biais algorithmiques, équité, transparence, explicabilité, responsabilité et protection des données."),

                subject("Soft Skills", "1",
                        "Développement des compétences relationnelles et organisationnelles utiles au travail en équipe.",
                        "Communication, collaboration, gestion du temps, résolution de problèmes et travail en équipe."),

                subject("TEC", "2",
                        "Développement des compétences de communication écrite et orale dans un contexte académique et professionnel.",
                        "Expression écrite, présentation orale, structuration d'un discours, rédaction et communication professionnelle."),

                subject("Conférences", null,
                        "Séances de présentation et d'échange autour de sujets scientifiques et professionnels.",
                        "Interventions thématiques, présentations de travaux et échanges avec des chercheurs et professionnels.")
        ));

        TeachingUnit p2sEur = getTeachingUnit(
                teachingUnitRepository, p2sS3,
                "UE spécifique parcours EUR"
        );

        addSubjects(subjectRepository, p2sEur, List.of(

                subject("Combinatoire et probabilités", null,
                        "Étude des outils mathématiques permettant de dénombrer des structures et de modéliser des phénomènes aléatoires.",
                        "Dénombrement, permutations, combinaisons, probabilités discrètes, variables aléatoires et raisonnements combinatoires."),

                subject("Le petit séminaire de combinatoire et probabilités", null,
                        "Séminaire consacré à la présentation et à la discussion de sujets liés à la combinatoire et aux probabilités.",
                        "Exposés thématiques, lecture de travaux mathématiques, présentation de résultats et discussions scientifiques.")
        ));

        // ==========================================
        // P2S — SEMESTER 4
        // ==========================================

        TeachingUnit p2sMinors = getTeachingUnit(
                teachingUnitRepository, p2sS4, "Mineures"
        );

        addSubjects(subjectRepository, p2sMinors, List.of(

                subject("Cartographie et OpenStreetMap", "3",
                        "Découverte des données géographiques et des outils permettant de les exploiter et de les représenter.",
                        "Données géographiques, OpenStreetMap, données vectorielles, formats géospatiaux, cartographie et APIs géographiques."),

                subject("Détection d'intrusions", "3",
                        "Étude des techniques permettant d'identifier des activités suspectes ou malveillantes dans les systèmes informatiques.",
                        "Menaces réseau, analyse du trafic, signatures, détection d'anomalies, systèmes IDS et évaluation des alertes."),

                subject("Maîtrise des crises dans les environnements critiques", "3",
                        "Étude des méthodes de préparation et de gestion des situations de crise dans les systèmes critiques.",
                        "Analyse des risques, gestion de crise, continuité d'activité, coordination, prise de décision et retour d'expérience."),

                subject("Monitoring de systèmes cyber-physiques", "3",
                        "Surveillance de systèmes combinant des composants informatiques, physiques et des processus en temps réel.",
                        "Systèmes cyber-physiques, acquisition de données, capteurs, supervision, détection d'anomalies et surveillance."),

                subject("Analyse des réseaux d'interactions", "3",
                        "Analyse des relations entre entités représentées sous forme de réseaux ou de graphes.",
                        "Théorie des graphes, réseaux sociaux, centralité, communautés, connexité et analyse des structures relationnelles."),

                subject("Apprentissage et métaapprentissage appliqués à l'IoT", "3",
                        "Application de méthodes d'apprentissage automatique aux données issues de l'Internet des objets.",
                        "Apprentissage automatique, métaapprentissage, données de capteurs, contraintes des objets connectés et adaptation des modèles."),

                subject("Cybersécurité & IA", "3",
                        "Étude des liens entre intelligence artificielle et cybersécurité, notamment pour la détection et l'analyse des menaces.",
                        "Apprentissage automatique appliqué à la sécurité, détection d'anomalies, classification des menaces et limites des modèles d'IA."),

                subject("Design et expérience utilisateur", "3",
                        "Introduction à la conception d'interfaces numériques centrées sur les besoins et les usages des utilisateurs.",
                        "UX, ergonomie, conception d'interfaces, prototypage, utilisabilité, tests utilisateurs et accessibilité."),

                subject("Informatique quantique", "3",
                        "Introduction aux principes de l'informatique quantique et aux modèles de calcul fondés sur la mécanique quantique.",
                        "Qubits, superposition, intrication, portes quantiques, circuits quantiques et algorithmes fondamentaux.")
        ));

        TeachingUnit p2sProfessionalization = getTeachingUnit(
                teachingUnitRepository, p2sS4, "Professionnalisation"
        );

        addSubjects(subjectRepository, p2sProfessionalization, List.of(

                subject("Stage Recherche/Développement", "24",
                        "Mise en pratique des compétences acquises durant le master au sein d'un projet de recherche ou de développement.",
                        "Travail sur un sujet encadré, étude bibliographique ou technique, conception, réalisation, expérimentation et rédaction d'un rapport.")
        ));

        // ==========================================
        // EI2D — SEMESTER 3
        // ==========================================

        TeachingUnit ei2dAI = getTeachingUnit(
                teachingUnitRepository, ei2dS3,
                "Majeure (I) : Apprentissage Artificiel et Science des Données"
        );

        addSubjects(subjectRepository, ei2dAI, List.of(

                subject("Apprentissage statistique", "3",
                        "Étude des méthodes statistiques permettant aux systèmes informatiques d'apprendre à partir de données.",
                        "Apprentissage supervisé, régression, classification, estimation, généralisation et évaluation des modèles."),

                subject("Deep Learning", "3",
                        "Étude des réseaux de neurones profonds utilisés pour apprendre des représentations complexes à partir de données.",
                        "Réseaux de neurones, rétropropagation, fonctions d'activation, optimisation, réseaux convolutifs et architectures profondes."),

                subject("Science des données numériques", "3",
                        "Étude des méthodes de traitement et d'analyse de données numériques pour en extraire des informations utiles.",
                        "Préparation des données, exploration, transformation, analyse statistique, visualisation et traitement de données."),

                subject("Statistique exploratoire multidimensionnelle", "3",
                        "Analyse statistique de jeux de données comportant plusieurs variables afin d'identifier leurs structures et relations.",
                        "Analyse en composantes principales, méthodes factorielles, réduction de dimension, classification et interprétation des résultats.")
        ));

        TeachingUnit ei2dData = getTeachingUnit(
                teachingUnitRepository, ei2dS3,
                "Majeure (II) : Ingénierie des Données et Informatique Décisionnelle"
        );

        addSubjects(subjectRepository, ei2dData, List.of(

                subject("Bases de données avancées", "3",
                        "Approfondissement des systèmes de gestion de bases de données et des techniques de manipulation de données complexes.",
                        "Modélisation, SQL avancé, transactions, indexation, optimisation des requêtes et gestion des bases de données."),

                subject("Data Mining", "3",
                        "Extraction de connaissances et de motifs pertinents à partir de grands ensembles de données.",
                        "Prétraitement, classification, clustering, règles d'association, évaluation et interprétation des résultats."),

                subject("Data Warehouse", "3",
                        "Étude des architectures permettant de centraliser et d'organiser les données pour l'analyse et la prise de décision.",
                        "Entrepôts de données, processus ETL, modélisation dimensionnelle, schémas en étoile et en flocon et OLAP."),

                subject("Informatique Décisionnelle", "3",
                        "Utilisation des systèmes et méthodes informatiques pour transformer les données en informations utiles à la décision.",
                        "Business Intelligence, indicateurs, tableaux de bord, reporting, analyse multidimensionnelle et aide à la décision.")
        ));

        TeachingUnit ei2dTransversal = getTeachingUnit(
                teachingUnitRepository, ei2dS3,
                "UE Transversales"
        );

        addSubjects(subjectRepository, ei2dTransversal, List.of(

                subject("Anglais", "2",
                        "Développement des compétences en anglais dans un contexte universitaire et professionnel.",
                        "Compréhension écrite et orale, expression, vocabulaire scientifique et technique et communication professionnelle."),

                subject("Éthique des algorithmes", "1",
                        "Réflexion sur les enjeux éthiques et sociétaux liés à la conception et à l'utilisation des algorithmes.",
                        "Biais algorithmiques, équité, transparence, explicabilité, responsabilité et protection des données."),

                subject("Soft Skills", "1",
                        "Développement des compétences relationnelles et organisationnelles utiles au travail en équipe.",
                        "Communication, collaboration, gestion du temps, résolution de problèmes et travail en équipe."),

                subject("TEC", "2",
                        "Développement des compétences de communication écrite et orale dans un contexte académique et professionnel.",
                        "Expression écrite, présentation orale, structuration d'un discours, rédaction et communication professionnelle."),

                subject("Conférences", null,
                        "Séances de présentation et d'échange autour de sujets scientifiques et professionnels.",
                        "Interventions thématiques, présentations de travaux et échanges avec des chercheurs et professionnels.")
        ));

        TeachingUnit ei2dEur = getTeachingUnit(
                teachingUnitRepository, ei2dS3,
                "UE spécifique parcours EUR"
        );

        addSubjects(subjectRepository, ei2dEur, List.of(

                subject("Combinatoire et probabilités", null,
                        "Étude des outils mathématiques permettant de dénombrer des structures et de modéliser des phénomènes aléatoires.",
                        "Dénombrement, permutations, combinaisons, probabilités discrètes, variables aléatoires et raisonnements combinatoires."),

                subject("Le petit séminaire de combinatoire et probabilités", null,
                        "Séminaire consacré à la présentation et à la discussion de sujets liés à la combinatoire et aux probabilités.",
                        "Exposés thématiques, lecture de travaux mathématiques, présentation de résultats et discussions scientifiques.")
        ));

        TeachingUnit ei2dOptional = getTeachingUnit(
                teachingUnitRepository, ei2dS3, "UE Facultatives"
        );

        addSubjects(subjectRepository, ei2dOptional, List.of(

                subject("UE Libre", null,
                        "Activité facultative permettant à l'étudiant de compléter son parcours universitaire.",
                        "Contenu variable selon l'activité choisie.")
        ));

        // ==========================================
        // EI2D — SEMESTER 4
        // ==========================================

        TeachingUnit ei2dMinors = getTeachingUnit(
                teachingUnitRepository, ei2dS4, "Mineures"
        );

        addSubjects(subjectRepository, ei2dMinors, List.of(

                subject("Apprentissage de représentations visuelles", "3",
                        "Étude de méthodes d'apprentissage permettant de construire des représentations utiles à partir de données visuelles.",
                        "Représentation des images, extraction de caractéristiques, réseaux de neurones, apprentissage de représentations et évaluation."),

                subject("Décision dans les systèmes multi-agents", "3",
                        "Étude des mécanismes de décision et de coordination entre plusieurs agents autonomes.",
                        "Agents, systèmes multi-agents, stratégies de décision, coordination, coopération et négociation."),

                subject("Données textuelles et modèles génératifs", "3",
                        "Étude du traitement des données textuelles et des modèles capables de générer du contenu à partir de données.",
                        "Prétraitement du texte, représentation vectorielle, traitement automatique du langage, modèles de langage et génération de texte."),

                subject("Explicabilité et Intelligence Artificielle", "3",
                        "Étude des méthodes permettant de comprendre et d'interpréter les décisions produites par les modèles d'intelligence artificielle.",
                        "Interprétabilité, explicabilité, importance des variables, méthodes locales et globales et analyse des décisions des modèles."),

                subject("Analyse des réseaux d'interactions", "3",
                        "Analyse des relations entre entités représentées sous forme de réseaux ou de graphes.",
                        "Théorie des graphes, réseaux sociaux, centralité, communautés, connexité et analyse des structures relationnelles."),

                subject("Apprentissage et métaapprentissage appliqués à l'IoT", "3",
                        "Application de méthodes d'apprentissage automatique aux données issues de l'Internet des objets.",
                        "Apprentissage automatique, métaapprentissage, données de capteurs, contraintes des objets connectés et adaptation des modèles."),

                subject("Cybersécurité & IA", "3",
                        "Étude des liens entre intelligence artificielle et cybersécurité, notamment pour la détection et l'analyse des menaces.",
                        "Apprentissage automatique appliqué à la sécurité, détection d'anomalies, classification des menaces et limites des modèles d'IA."),

                subject("Design et expérience utilisateur", "3",
                        "Introduction à la conception d'interfaces numériques centrées sur les besoins et les usages des utilisateurs.",
                        "UX, ergonomie, conception d'interfaces, prototypage, utilisabilité, tests utilisateurs et accessibilité."),

                subject("Informatique quantique", "3",
                        "Introduction aux principes de l'informatique quantique et aux modèles de calcul fondés sur la mécanique quantique.",
                        "Qubits, superposition, intrication, portes quantiques, circuits quantiques et algorithmes fondamentaux.")
        ));

        TeachingUnit ei2dProfessionalization = getTeachingUnit(
                teachingUnitRepository, ei2dS4, "Professionnalisation"
        );

        addSubjects(subjectRepository, ei2dProfessionalization, List.of(

                subject("Stage Recherche/Développement", "24",
                        "Mise en pratique des compétences acquises durant le master au sein d'un projet de recherche ou de développement.",
                        "Travail sur un sujet encadré, étude bibliographique ou technique, conception, réalisation, expérimentation et rédaction d'un rapport.")
        ));
    }

    // ==========================================
    // SUBJECT HELPERS
    // ==========================================

    private SubjectSeed subject(
            String name,
            String ects,
            String description,
            String content) {

        return new SubjectSeed(
                name,
                ects == null ? null : new BigDecimal(ects),
                description,
                content
        );
    }

    private record SubjectSeed(
            String name,
            BigDecimal ects,
            String description,
            String content) {}

    private void addSubjects(
            SubjectRepository subjectRepository,
            TeachingUnit teachingUnit,
            List<SubjectSeed> subjects) {

        for (SubjectSeed seed : subjects) {

            Subject existing = subjectRepository
                    .findByTeachingUnitId(teachingUnit.getId())
                    .stream()
                    .filter(s -> s.getName().equals(seed.name()))
                    .findFirst()
                    .orElse(null);

            if (existing == null) {

                subjectRepository.save(
                        new Subject(
                                teachingUnit,
                                seed.name(),
                                seed.description(),
                                seed.content(),
                                seed.ects()
                        )
                );

            } else {

                existing.setDescription(seed.description());
                existing.setContent(seed.content());
                existing.setEcts(seed.ects());

                subjectRepository.save(existing);
            }
        }
    }

    private TeachingUnit getTeachingUnit(
            TeachingUnitRepository repository,
            Semester semester,
            String name) {

        return repository.findBySemesterId(semester.getId())
                .stream()
                .filter(unit -> unit.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Teaching Unit not found: " + name
                ));
    }

    // ==========================================
    // PARCOURS AND SEMESTER HELPERS
    // ==========================================

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
