package com.eduadvice.backend.config;

import com.eduadvice.backend.entity.*;
import com.eduadvice.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DataInitializerService {

    private final ParcoursRepository parcoursRepository;
    private final CareerOpportunityRepository opportunityRepository;
    private final SemesterRepository semesterRepository;
    private final TeachingUnitRepository teachingUnitRepository;
    private final SubjectRepository subjectRepository;

    // Constructor injection
    public DataInitializerService(
            ParcoursRepository parcoursRepository,
            CareerOpportunityRepository opportunityRepository,
            SemesterRepository semesterRepository,
            TeachingUnitRepository teachingUnitRepository,
            SubjectRepository subjectRepository
    ) {
        this.parcoursRepository = parcoursRepository;
        this.opportunityRepository = opportunityRepository;
        this.semesterRepository = semesterRepository;
        this.teachingUnitRepository = teachingUnitRepository;
        this.subjectRepository = subjectRepository;
    }

    @Transactional
    public void initialize() {
        initializeParcours();

        initializeCareerOpportunities();

        initializeSemesters();

        initializeTeachingUnits();

        initializeSubjects();

        initializeCareerOpportunitiesAssociations();
    }

    private void initializeParcours() {
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
    }

    private void initializeCareerOpportunities() {
        // Initialize Career Opportunities
        if (opportunityRepository.count() == 0) {

            List<CareerOpportunity> opportunities = List.of(

                    new CareerOpportunity(
                            "Data Analyst",
                            "Data",
                            "Analyzes datasets to identify trends, patterns and insights that support business decisions.",
                            "SQL, Python, Excel, Power BI, Statistics"
                    ),

                    new CareerOpportunity(
                            "Data Scientist",
                            "Data",
                            "Develops statistical models and machine learning algorithms to extract insights and make predictions.",
                            "Python, Machine Learning, Statistics, Pandas, Scikit-learn"
                    ),

                    new CareerOpportunity(
                            "Data Engineer",
                            "Data Engineering",
                            "Designs and maintains data pipelines and infrastructure used to collect, process and store large datasets.",
                            "Python, SQL, ETL, Spark, PostgreSQL"
                    ),

                    new CareerOpportunity(
                            "Business Intelligence Analyst",
                            "Business Intelligence",
                            "Transforms business data into reports and dashboards to help organizations make informed decisions.",
                            "SQL, Power BI, Tableau, Excel, Data Modeling"
                    ),

                    new CareerOpportunity(
                            "Software Engineer",
                            "Software Development",
                            "Designs, develops and maintains software applications.",
                            "Java, OOP, Spring Boot, Git, Testing"
                    ),

                    new CareerOpportunity(
                            "Cybersecurity Analyst",
                            "Cybersecurity",
                            "Monitors information systems, detects security threats and helps prevent cyberattacks.",
                            "Networking, Linux, SIEM, Security, Incident Response"
                    ),

                    new CareerOpportunity(
                            "Backend Developer",
                            "Software Development",
                            "Develops server-side applications, APIs and database integrations.",
                            "Java, Spring Boot, REST API, PostgreSQL, Git"
                    )
            );

            opportunityRepository.saveAll(opportunities);
        }
    }

    private void initializeSemesters() {
        //Initialize Semester
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
    }

    private void initializeTeachingUnits() {
        // Initialize TeachingUnits
        if (teachingUnitRepository.count() == 0) {

            Parcours p2s = parcoursRepository.findAll().stream()
                    .filter(p -> p.getName().equals("P2S"))
                    .findFirst()
                    .orElseThrow();

            Semester p2sS3 = semesterRepository.findAll().stream()
                    .filter(s -> s.getParcours().getId().equals(p2s.getId()))
                    .filter(s -> s.getNumber() == 3)
                    .findFirst()
                    .orElseThrow();

            List<TeachingUnit> teachingUnits = List.of(

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
                    )
            );

            teachingUnitRepository.saveAll(teachingUnits);
        }
    }

    private void initializeSubjects() {
        // Initialize Subjects
        if (subjectRepository.count() == 0) {

            // Retrieve TeachingUnits
            Parcours p2s = parcoursRepository.findAll().stream()
                    .filter(p -> p.getName().equals("P2S"))
                    .findFirst()
                    .orElseThrow();

            Semester p2sS3 = semesterRepository.findAll().stream()
                    .filter(s -> s.getParcours().getId().equals(p2s.getId()))
                    .filter(s -> s.getNumber() == 3)
                    .findFirst()
                    .orElseThrow();

            List<TeachingUnit> teachingUnits =
                    teachingUnitRepository.findBySemesterId(p2sS3.getId());

            TeachingUnit p2sProgramming = teachingUnits.stream()
                    .filter(t -> t.getName().equals("Majeure I : Programmation"))
                    .findFirst()
                    .orElseThrow();

            TeachingUnit p2sSpecification = teachingUnits.stream()
                    .filter(t -> t.getName().equals("Majeure II : Spécification, Sûreté et Sécurité"))
                    .findFirst()
                    .orElseThrow();

            TeachingUnit p2sTransversal = teachingUnits.stream()
                    .filter(t -> t.getName().equals("UE transversales"))
                    .findFirst()
                    .orElseThrow();

            // P2S - Semester 3 - Programming
            List<Subject> programmingSubjects = List.of(

                    new Subject(
                            p2sProgramming,
                            "Développement JavaScript avancé et ubiquitaire",
                            "Approfondissement du développement JavaScript pour concevoir des applications exécutées dans différents environnements.",
                            "JavaScript avancé, programmation asynchrone, APIs, environnements d'exécution et développement d'applications web et multiplateformes.",
                            new BigDecimal("3")
                    ),

                    new Subject(
                            p2sProgramming,
                            "Environnements de développements libres",
                            "Découverte et utilisation des environnements et outils de développement fondés sur les logiciels libres.",
                            "Écosystème open source, outils de développement, systèmes Linux, gestion de versions, compilation et automatisation.",
                            new BigDecimal("3")
                    ),

                    new Subject(
                            p2sProgramming,
                            "Méthodologies et langages avancés",
                            "Étude de méthodes et de langages permettant de concevoir des logiciels complexes de manière structurée.",
                            "Paradigmes de programmation, abstraction, conception logicielle, systèmes de types et utilisation de langages avancés.",
                            new BigDecimal("3")
                    ),

                    new Subject(
                            p2sProgramming,
                            "Programmation modulaire et résiliente",
                            "Conception de logiciels organisés en composants indépendants et capables de mieux gérer les erreurs et les défaillances.",
                            "Modularité, interfaces, séparation des responsabilités, gestion des erreurs, tolérance aux pannes et robustesse logicielle.",
                            new BigDecimal("3")
                    )
            );

            // P2S - Semester 3 - Specification
            List<Subject> specificationSubjects = List.of(

                    new Subject(
                            p2sSpecification,
                            "Programmes et preuves",
                            "Étude des méthodes formelles permettant de raisonner sur la correction des programmes.",
                            "Logique, assertions, préconditions, postconditions, invariants, preuves de programmes et vérification de propriétés.",
                            new BigDecimal("3")
                    ),

                    new Subject(
                            p2sSpecification,
                            "Réseaux de Petri de haut niveau",
                            "Modélisation et analyse de systèmes concurrents et distribués à l'aide de réseaux de Petri.",
                            "Places, transitions, jetons, réseaux colorés, synchronisation, concurrence, propriétés comportementales et analyse de modèles.",
                            new BigDecimal("3")
                    ),

                    new Subject(
                            p2sSpecification,
                            "Spécification et vérification de systèmes complexes",
                            "Méthodes de description formelle et de vérification du comportement de systèmes informatiques complexes.",
                            "Spécification, modélisation, propriétés de systèmes, vérification formelle, model checking et détection d'incohérences.",
                            new BigDecimal("3")
                    ),

                    new Subject(
                            p2sSpecification,
                            "Sûreté et sécurité",
                            "Étude des principes permettant de concevoir des systèmes fiables et de protéger leurs composants et leurs données.",
                            "Sûreté de fonctionnement, sécurité informatique, menaces, vulnérabilités, analyse des risques et mécanismes de protection.",
                            new BigDecimal("3")
                    )
            );

            // P2S - Semester 3 - Transversal
            List<Subject> transversalSubjects = List.of(

                    new Subject(
                            p2sTransversal,
                            "Anglais",
                            "Développement des compétences en anglais dans un contexte universitaire et professionnel.",
                            "Compréhension écrite et orale, expression, vocabulaire scientifique et technique et communication professionnelle.",
                            new BigDecimal("2")
                    ),

                    new Subject(
                            p2sTransversal,
                            "Éthique des algorithmes",
                            "Réflexion sur les enjeux éthiques et sociétaux liés à la conception et à l'utilisation des algorithmes.",
                            "Biais algorithmiques, équité, transparence, explicabilité, responsabilité et protection des données.",
                            new BigDecimal("1")
                    ),

                    new Subject(
                            p2sTransversal,
                            "Soft Skills",
                            "Développement des compétences relationnelles et organisationnelles utiles au travail en équipe.",
                            "Communication, collaboration, gestion du temps, résolution de problèmes et travail en équipe.",
                            new BigDecimal("1")
                    ),

                    new Subject(
                            p2sTransversal,
                            "TEC",
                            "Développement des compétences de communication écrite et orale dans un contexte académique et professionnel.",
                            "Expression écrite, présentation orale, structuration d'un discours, rédaction et communication professionnelle.",
                            new BigDecimal("2")
                    ),

                    new Subject(
                            p2sTransversal,
                            "Conférences",
                            "Séances de présentation et d'échange autour de sujets scientifiques et professionnels.",
                            "Interventions thématiques, présentations de travaux et échanges avec des chercheurs et professionnels.",
                            null
                    )
            );

            subjectRepository.saveAll(programmingSubjects);
            subjectRepository.saveAll(specificationSubjects);
            subjectRepository.saveAll(transversalSubjects);
        }
    }

        // INITIALIZE MANY-TO-MANY ASSOCIATIONS

    private void initializeCareerOpportunitiesAssociations() {
        // Retrieve entities
        List<Parcours> parcoursList = parcoursRepository.findAll();

        List<CareerOpportunity> opportunities =
                opportunityRepository.findAll();

        Parcours ei2d = parcoursList.stream()
                .filter(p -> p.getName().equals("EI2D"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Parcours EI2D not found"
                ));

        Parcours p2s = parcoursList.stream()
                .filter(p -> p.getName().equals("P2S"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Parcours P2S not found"
                ));

        // Create Many-to-Many associations
        for (CareerOpportunity opportunity : opportunities) {

            switch (opportunity.getTitle()) {

                case "Data Analyst",
                     "Data Scientist",
                     "Business Intelligence Analyst" ->

                        ei2d.addCareerOpportunity(opportunity);

                case "Software Engineer",
                     "Backend Developer",
                     "Cybersecurity Analyst" ->

                        p2s.addCareerOpportunity(opportunity);

                case  "Data Engineer" -> {
                    p2s.addCareerOpportunity(opportunity);
                    ei2d.addCareerOpportunity(opportunity);
                }
            }
        }

        // Save associations
        parcoursRepository.saveAll(parcoursList);
    }
}
