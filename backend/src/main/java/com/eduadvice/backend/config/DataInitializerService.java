package com.eduadvice.backend.config;

import com.eduadvice.backend.entity.CareerOpportunity;
import com.eduadvice.backend.entity.Parcours;
import com.eduadvice.backend.entity.Semester;
import com.eduadvice.backend.repository.CareerOpportunityRepository;
import com.eduadvice.backend.repository.ParcoursRepository;
import com.eduadvice.backend.repository.SemesterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataInitializerService {

    private final ParcoursRepository parcoursRepository;
    private final CareerOpportunityRepository opportunityRepository;
    private final SemesterRepository semesterRepository;

    // Constructor injection
    public DataInitializerService(
            ParcoursRepository parcoursRepository,
            CareerOpportunityRepository opportunityRepository, SemesterRepository semesterRepository) {

        this.parcoursRepository = parcoursRepository;
        this.opportunityRepository = opportunityRepository;
        this.semesterRepository = semesterRepository;
    }

    @Transactional
    public void initialize() {
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
