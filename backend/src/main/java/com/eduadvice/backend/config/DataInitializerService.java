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

            Parcours ei2d = parcoursRepository.findAll().stream()
                    .filter(p -> p.getName().equals("EI2D"))
                    .findFirst()
                    .orElseThrow();

            Parcours p2s = parcoursRepository.findAll().stream()
                    .filter(p -> p.getName().equals("P2S"))
                    .findFirst()
                    .orElseThrow();

            Semester ei2dS3 = semesterRepository.findAll().stream()
                    .filter(s -> s.getParcours().getId().equals(ei2d.getId()))
                    .filter(s -> s.getNumber() == 3)
                    .findFirst()
                    .orElseThrow();

            Semester ei2dS4 = semesterRepository.findAll().stream()
                    .filter(s -> s.getParcours().getId().equals(ei2d.getId()))
                    .filter(s -> s.getNumber() == 4)
                    .findFirst()
                    .orElseThrow();


            Semester p2sS3 = semesterRepository.findAll().stream()
                    .filter(s -> s.getParcours().getId().equals(p2s.getId()))
                    .filter(s -> s.getNumber() == 3)
                    .findFirst()
                    .orElseThrow();

            Semester p2sS4 = semesterRepository.findAll().stream()
                    .filter(s -> s.getParcours().getId().equals(p2s.getId()))
                    .filter(s -> s.getNumber() == 4)
                    .findFirst()
                    .orElseThrow();

            List<TeachingUnit> teachingUnits = List.of(
                    // EI2D — Semester 3

                    new TeachingUnit(
                            ei2dS3,
                            "Majeure I : Apprentissage Artificiel et Science des Données",
                            TeachingUnitCategory.MAJOR_1,
                            new BigDecimal("12")
                    ),

                    new TeachingUnit(
                            ei2dS3,
                            "Majeure II : Ingénierie des Données et Informatique Décisionnelle",
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
                    ),

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
                    )
            );

            teachingUnitRepository.saveAll(teachingUnits);
        }
    }

    private void initializeSubjects() {
        // Initialize Subjects
        if (subjectRepository.count() == 0) {
            //-------
            // Retrieve TeachingUnits
            //-------
            Parcours ei2d = parcoursRepository.findAll().stream()
                    .filter(p -> p.getName().equals("EI2D"))
                    .findFirst()
                    .orElseThrow();

            Parcours p2s = parcoursRepository.findAll().stream()
                    .filter(p -> p.getName().equals("P2S"))
                    .findFirst()
                    .orElseThrow();

            Semester ei2dS3 = semesterRepository.findAll().stream()
                    .filter(s -> s.getParcours().getId().equals(ei2d.getId()))
                    .filter(s -> s.getNumber() == 3)
                    .findFirst()
                    .orElseThrow();

            Semester ei2dS4 = semesterRepository.findAll().stream()
                    .filter(s -> s.getParcours().getId().equals(ei2d.getId()))
                    .filter(s -> s.getNumber() == 4)
                    .findFirst()
                    .orElseThrow();

            Semester p2sS3 = semesterRepository.findAll().stream()
                    .filter(s -> s.getParcours().getId().equals(p2s.getId()))
                    .filter(s -> s.getNumber() == 3)
                    .findFirst()
                    .orElseThrow();

            Semester p2sS4 = semesterRepository.findAll().stream()
                    .filter(s -> s.getParcours().getId().equals(p2s.getId()))
                    .filter(s -> s.getNumber() == 4)
                    .findFirst()
                    .orElseThrow();

            //-----
            // EI2D Semester 3
            //-----
            List<TeachingUnit> teachingUnitsEI2Ds3 =
                    teachingUnitRepository.findBySemesterId(ei2dS3.getId());

            TeachingUnit ei2dAI = teachingUnitsEI2Ds3.stream()
                    .filter(t -> t.getName().equals("Majeure I : Apprentissage Artificiel et Science des Données"))
                    .findFirst()
                    .orElseThrow();

            TeachingUnit ei2dData = teachingUnitsEI2Ds3.stream()
                    .filter(t -> t.getName().equals("Majeure II : Ingénierie des Données et Informatique Décisionnelle"))
                    .findFirst()
                    .orElseThrow();

            TeachingUnit ei2dTransversal = teachingUnitsEI2Ds3.stream()
                    .filter(t -> t.getName().equals("UE Transversales"))
                    .findFirst()
                    .orElseThrow();

            TeachingUnit ei2dEur = teachingUnitsEI2Ds3.stream()
                    .filter(t -> t.getName().equals("UE spécifique parcours EUR"))
                    .findFirst()
                    .orElseThrow();

            List<Subject> AISubjectsEI2D = List.of(
                    new Subject(
                            ei2dAI,
                            "Apprentissage statistique",
                            "Étude des méthodes statistiques permettant aux systèmes informatiques d'apprendre à partir de données.",
                            "Apprentissage supervisé, régression, classification, estimation, généralisation et évaluation des modèles.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dAI,
                            "Deep Learning",
                            "Étude des réseaux de neurones profonds utilisés pour apprendre des représentations complexes à partir de données.",
                            "Réseaux de neurones, rétropropagation, fonctions d'activation, optimisation, réseaux convolutifs et architectures profondes.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dAI,
                            "Science des données numériques",
                            "Étude des méthodes de traitement et d'analyse de données numériques pour en extraire des informations utiles.",
                            "Préparation des données, exploration, transformation, analyse statistique, visualisation et traitement de données.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dAI,
                            "Statistique exploratoire multidimensionnelle",
                            "Analyse statistique de jeux de données comportant plusieurs variables afin d'identifier leurs structures et relations.",
                            "Analyse en composantes principales, méthodes factorielles, réduction de dimension, classification et interprétation des résultats.",
                            new BigDecimal("3"))
            );

            List<Subject> DataSubjectsEI2D = List.of(
                    new Subject(
                            ei2dData,
                            "Bases de données avancées",
                            "Approfondissement des systèmes de gestion de bases de données et des techniques de manipulation de données complexes.",
                            "Modélisation, SQL avancé, transactions, indexation, optimisation des requêtes et gestion des bases de données.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dData,
                            "Data Mining",
                            "Extraction de connaissances et de motifs pertinents à partir de grands ensembles de données.",
                            "Prétraitement, classification, clustering, règles d'association, évaluation et interprétation des résultats.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dData,
                            "Data Warehouse",
                            "Étude des architectures permettant de centraliser et d'organiser les données pour l'analyse et la prise de décision.",
                            "Entrepôts de données, processus ETL, modélisation dimensionnelle, schémas en étoile et en flocon et OLAP.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dData,
                            "Informatique Décisionnelle",
                            "Utilisation des systèmes et méthodes informatiques pour transformer les données en informations utiles à la décision.",
                            "Business Intelligence, indicateurs, tableaux de bord, reporting, analyse multidimensionnelle et aide à la décision.",
                            new BigDecimal("3"))

            );

            List<Subject> transversalSubjectsEI2D = List.of(
                    new Subject(
                            ei2dTransversal,
                            "Anglais",
                            "Développement des compétences en anglais dans un contexte universitaire et professionnel.",
                            "Compréhension écrite et orale, expression, vocabulaire scientifique et technique et communication professionnelle.",
                            new BigDecimal("2")
                    ),

                    new Subject(
                            ei2dTransversal,
                            "Éthique des algorithmes",
                            "Réflexion sur les enjeux éthiques et sociétaux liés à la conception et à l'utilisation des algorithmes.",
                            "Biais algorithmiques, équité, transparence, explicabilité, responsabilité et protection des données.",
                            new BigDecimal("1")
                    ),

                    new Subject(
                            ei2dTransversal,
                            "Soft Skills",
                            "Développement des compétences relationnelles et organisationnelles utiles au travail en équipe.",
                            "Communication, collaboration, gestion du temps, résolution de problèmes et travail en équipe.",
                            new BigDecimal("1")
                    ),

                    new Subject(
                            ei2dTransversal,
                            "TEC",
                            "Développement des compétences de communication écrite et orale dans un contexte académique et professionnel.",
                            "Expression écrite, présentation orale, structuration d'un discours, rédaction et communication professionnelle.",
                            new BigDecimal("2")
                    ),

                    new Subject(
                            ei2dTransversal,
                            "Conférences",
                            "Séances de présentation et d'échange autour de sujets scientifiques et professionnels.",
                            "Interventions thématiques, présentations de travaux et échanges avec des chercheurs et professionnels.",
                            null
                    )
            );

            List<Subject>  eurSubjectsEI2D = List.of(
                    new Subject(
                            ei2dEur,
                            "Combinatoire et probabilités",
                            "Étude des outils mathématiques permettant de dénombrer des structures et de modéliser des phénomènes aléatoires.",
                            "Dénombrement, permutations, combinaisons, probabilités discrètes, variables aléatoires et raisonnements combinatoires.",
                            null),

                    new Subject(
                            ei2dEur,
                            "Le petit séminaire de combinatoire et probabilités",
                            "Séminaire consacré à la présentation et à la discussion de sujets liés à la combinatoire et aux probabilités.",
                            "Exposés thématiques, lecture de travaux mathématiques, présentation de résultats et discussions scientifiques.",
                            null)
            );

            //-----
            // EI2D Semester 4
            //-----
            List<TeachingUnit> teachingUnitsEI2Ds4 =
                    teachingUnitRepository.findBySemesterId(ei2dS4.getId());

            TeachingUnit ei2dMineures = teachingUnitsEI2Ds4.stream()
                    .filter(t -> t.getName().equals("Mineures"))
                    .findFirst()
                    .orElseThrow();

            TeachingUnit ei2dProfessionnalisation = teachingUnitsEI2Ds4.stream()
                    .filter(t -> t.getName().equals("Professionnalisation"))
                    .findFirst()
                    .orElseThrow();

            List<Subject> mineuresSubjectEI2D = List.of(
                    new Subject(
                            ei2dMineures,
                            "Apprentissage de représentations visuelles",
                            "Étude de méthodes d'apprentissage permettant de construire des représentations utiles à partir de données visuelles.",
                            "Représentation des images, extraction de caractéristiques, réseaux de neurones, apprentissage de représentations et évaluation.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dMineures,
                            "Décision dans les systèmes multi-agents",
                            "Étude des mécanismes de décision et de coordination entre plusieurs agents autonomes.",
                            "Agents, systèmes multi-agents, stratégies de décision, coordination, coopération et négociation.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dMineures,
                            "Données textuelles et modèles génératifs",
                            "Étude du traitement des données textuelles et des modèles capables de générer du contenu à partir de données.",
                            "Prétraitement du texte, représentation vectorielle, traitement automatique du langage, modèles de langage et génération de texte.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dMineures,
                            "Explicabilité et Intelligence Artificielle",
                            "Étude des méthodes permettant de comprendre et d'interpréter les décisions produites par les modèles d'intelligence artificielle.",
                            "Interprétabilité, explicabilité, importance des variables, méthodes locales et globales et analyse des décisions des modèles.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dMineures,
                            "Analyse des réseaux d'interactions",
                            "Analyse des relations entre entités représentées sous forme de réseaux ou de graphes.",
                            "Théorie des graphes, réseaux sociaux, centralité, communautés, connexité et analyse des structures relationnelles.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dMineures,
                            "Apprentissage et métaapprentissage appliqués à l'IoT",
                            "Application de méthodes d'apprentissage automatique aux données issues de l'Internet des objets.",
                            "Apprentissage automatique, métaapprentissage, données de capteurs, contraintes des objets connectés et adaptation des modèles.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dMineures,
                            "Cybersécurité & IA",
                            "Étude des liens entre intelligence artificielle et cybersécurité, notamment pour la détection et l'analyse des menaces.",
                            "Apprentissage automatique appliqué à la sécurité, détection d'anomalies, classification des menaces et limites des modèles d'IA.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dMineures,
                            "Design et expérience utilisateur",
                            "Introduction à la conception d'interfaces numériques centrées sur les besoins et les usages des utilisateurs.",
                            "UX, ergonomie, conception d'interfaces, prototypage, utilisabilité, tests utilisateurs et accessibilité.",
                            new BigDecimal("3")),

                    new Subject(
                            ei2dMineures,
                            "Informatique quantique",
                            "Introduction aux principes de l'informatique quantique et aux modèles de calcul fondés sur la mécanique quantique.",
                            "Qubits, superposition, intrication, portes quantiques, circuits quantiques et algorithmes fondamentaux.",
                            new BigDecimal("3"))
            );

            List<Subject> professionalizationSubjectEI2D = List.of(
                    new Subject(ei2dProfessionnalisation,
                            "Stage Recherche/Développement",
                            "Mise en pratique des compétences acquises durant le master au sein d'un projet de recherche ou de développement.",
                            "Travail sur un sujet encadré, étude bibliographique ou technique, conception, réalisation, expérimentation et rédaction d'un rapport.",
                            new BigDecimal("24"))
            );

            //-----
            // P2S Semester 3
            //-----
            List<TeachingUnit> teachingUnitsP2Ss3 =
                    teachingUnitRepository.findBySemesterId(p2sS3.getId());

            TeachingUnit p2sProgramming = teachingUnitsP2Ss3.stream()
                    .filter(t -> t.getName().equals("Majeure I : Programmation"))
                    .findFirst()
                    .orElseThrow();

            TeachingUnit p2sSpecification = teachingUnitsP2Ss3.stream()
                    .filter(t -> t.getName().equals("Majeure II : Spécification, Sûreté et Sécurité"))
                    .findFirst()
                    .orElseThrow();

            TeachingUnit p2sTransversal = teachingUnitsP2Ss3.stream()
                    .filter(t -> t.getName().equals("UE transversales"))
                    .findFirst()
                    .orElseThrow();

            TeachingUnit p2sEur = teachingUnitsP2Ss3.stream()
                    .filter(t -> t.getName().equals("UE spécifique parcours EUR"))
                    .findFirst()
                    .orElseThrow();

            // P2S - Semester 3 - Programming
            List<Subject> programmingSubjectsP2S = List.of(

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
            List<Subject> specificationSubjectsP2S = List.of(

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
            List<Subject> transversalSubjectsP2S = List.of(

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

            List<Subject> eurSubjectsP2S = List.of(
                   new Subject(
                           p2sEur,
                           "Combinatoire et probabilités",
                           "Étude des outils mathématiques permettant de dénombrer des structures et de modéliser des phénomènes aléatoires.",
                           "Dénombrement, permutations, combinaisons, probabilités discrètes, variables aléatoires et raisonnements combinatoires.",
                           null),

                   new Subject(
                           p2sEur,
                           "Le petit séminaire de combinatoire et probabilités",
                           "Séminaire consacré à la présentation et à la discussion de sujets liés à la combinatoire et aux probabilités.",
                           "Exposés thématiques, lecture de travaux mathématiques, présentation de résultats et discussions scientifiques.",
                           null)
            );

            //-----
            // P2S Semester 4
            //-----
            List<TeachingUnit> teachingUnitsP2Ss4 =
                    teachingUnitRepository.findBySemesterId(p2sS4.getId());

            TeachingUnit p2sMineures = teachingUnitsP2Ss4.stream()
                    .filter(t -> t.getName().equals("Mineures"))
                    .findFirst()
                    .orElseThrow();

            TeachingUnit p2sProfessionnalisation = teachingUnitsP2Ss4.stream()
                    .filter(t -> t.getName().equals("Professionnalisation"))
                    .findFirst()
                    .orElseThrow();

            List<Subject> mineursSubjectP2S = List.of(
                    new Subject(
                            p2sMineures,
                            "Cartographie et OpenStreetMap",
                            "Découverte des données géographiques et des outils permettant de les exploiter et de les représenter.",
                            "Données géographiques, OpenStreetMap, données vectorielles, formats géospatiaux, cartographie et APIs géographiques.",
                            new BigDecimal("3")),

                    new Subject(
                            p2sMineures,
                            "Détection d'intrusions",
                            "Étude des techniques permettant d'identifier des activités suspectes ou malveillantes dans les systèmes informatiques.",
                            "Menaces réseau, analyse du trafic, signatures, détection d'anomalies, systèmes IDS et évaluation des alertes.",
                            new BigDecimal("3")),

                    new Subject(
                            p2sMineures,
                            "Maîtrise des crises dans les environnements critiques",
                            "Étude des méthodes de préparation et de gestion des situations de crise dans les systèmes critiques.",
                            "Analyse des risques, gestion de crise, continuité d'activité, coordination, prise de décision et retour d'expérience.",
                            new BigDecimal("3")),

                    new Subject(
                            p2sMineures,
                            "Monitoring de systèmes cyber-physiques",
                            "Surveillance de systèmes combinant des composants informatiques, physiques et des processus en temps réel.",
                            "Systèmes cyber-physiques, acquisition de données, capteurs, supervision, détection d'anomalies et surveillance.",
                            new BigDecimal("3")),

                    new Subject(
                            p2sMineures,
                            "Analyse des réseaux d'interactions",
                            "Analyse des relations entre entités représentées sous forme de réseaux ou de graphes.",
                            "Théorie des graphes, réseaux sociaux, centralité, communautés, connexité et analyse des structures relationnelles.",
                            new BigDecimal("3")),

                    new Subject(
                            p2sMineures,
                            "Apprentissage et métaapprentissage appliqués à l'IoT",
                            "Application de méthodes d'apprentissage automatique aux données issues de l'Internet des objets.",
                            "Apprentissage automatique, métaapprentissage, données de capteurs, contraintes des objets connectés et adaptation des modèles.",
                            new BigDecimal("3")),

                    new Subject(
                            p2sMineures,
                            "Cybersécurité & IA",
                            "Étude des liens entre intelligence artificielle et cybersécurité, notamment pour la détection et l'analyse des menaces.",
                            "Apprentissage automatique appliqué à la sécurité, détection d'anomalies, classification des menaces et limites des modèles d'IA.",
                            new BigDecimal("3")),

                    new Subject(
                            p2sMineures,
                            "Design et expérience utilisateur",
                            "Introduction à la conception d'interfaces numériques centrées sur les besoins et les usages des utilisateurs.",
                            "UX, ergonomie, conception d'interfaces, prototypage, utilisabilité, tests utilisateurs et accessibilité.",
                            new BigDecimal("3")),

                    new Subject(
                            p2sMineures,
                            "Informatique quantique",
                            "Introduction aux principes de l'informatique quantique et aux modèles de calcul fondés sur la mécanique quantique.",
                            "Qubits, superposition, intrication, portes quantiques, circuits quantiques et algorithmes fondamentaux.",
                            new BigDecimal("3"))
            );

            List<Subject> professionalizationSubjectP2S = List.of(
                    new Subject(p2sProfessionnalisation,
                            "Stage Recherche/Développement",
                            "Mise en pratique des compétences acquises durant le master au sein d'un projet de recherche ou de développement.",
                            "Travail sur un sujet encadré, étude bibliographique ou technique, conception, réalisation, expérimentation et rédaction d'un rapport.",
                            new BigDecimal("24"))
            );

            subjectRepository.saveAll(AISubjectsEI2D);
            subjectRepository.saveAll(DataSubjectsEI2D);
            subjectRepository.saveAll(transversalSubjectsEI2D);
            subjectRepository.saveAll(eurSubjectsEI2D);
            subjectRepository.saveAll(mineuresSubjectEI2D);
            subjectRepository.saveAll(professionalizationSubjectEI2D);

            subjectRepository.saveAll(programmingSubjectsP2S);
            subjectRepository.saveAll(specificationSubjectsP2S);
            subjectRepository.saveAll(transversalSubjectsP2S);
            subjectRepository.saveAll(eurSubjectsP2S);
            subjectRepository.saveAll(mineursSubjectP2S);
            subjectRepository.saveAll(professionalizationSubjectP2S);
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
