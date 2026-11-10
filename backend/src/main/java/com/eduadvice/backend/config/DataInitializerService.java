package com.eduadvice.backend.config;

import com.eduadvice.backend.entity.CareerOpportunity;
import com.eduadvice.backend.entity.Parcours;
import com.eduadvice.backend.repository.CareerOpportunityRepository;
import com.eduadvice.backend.repository.ParcoursRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataInitializerService {

    private final ParcoursRepository parcoursRepository;
    private final CareerOpportunityRepository opportunityRepository;

    // Constructor injection
    public DataInitializerService(
            ParcoursRepository parcoursRepository,
            CareerOpportunityRepository opportunityRepository) {

        this.parcoursRepository = parcoursRepository;
        this.opportunityRepository = opportunityRepository;
    }

    @Transactional
    public void initialize() {

        // =====================================
        // 1. Initialize Parcours
        // =====================================

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

        // =====================================
        // 2. Initialize Career Opportunities
        // =====================================

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
                            "Security Engineer",
                            "Cybersecurity",
                            "Designs and implements security solutions to protect systems, networks and applications.",
                            "Network Security, Linux, Firewalls, Cryptography, Python"
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

        // =====================================
        // 3. Retrieve entities
        // =====================================

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

        // =====================================
        // 4. Create Many-to-Many associations
        // =====================================

        for (CareerOpportunity opportunity : opportunities) {

            switch (opportunity.getTitle()) {

                case "Data Analyst",
                     "Data Scientist",
                     "Data Engineer",
                     "Business Intelligence Analyst" ->

                        ei2d.addCareerOpportunity(opportunity);

                case "Software Engineer",
                     "Backend Developer",
                     "Cybersecurity Analyst",
                     "Security Engineer" ->

                        p2s.addCareerOpportunity(opportunity);
            }
        }

        // =====================================
        // 5. Save associations
        // =====================================

        parcoursRepository.saveAll(parcoursList);
    }
}
