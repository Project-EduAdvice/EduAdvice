package com.eduadvice.backend.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class CareerOpportunity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String title;

    @Basic
    private String sector;

    @Column(columnDefinition ="TEXT")
    private String description;

    @Column(columnDefinition ="TEXT")
    private String required_skills;

    @ManyToMany(mappedBy = "careerOpportunities")
    private Set<Parcours> parcours = new HashSet<>();

    public CareerOpportunity() {
    }

    public CareerOpportunity(String title, String sector, String description, String required_skills) {
        this.title = title;
        this.sector = sector;
        this.description = description;
        this.required_skills = required_skills;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequired_skills() {
        return required_skills;
    }

    public void setRequired_skills(String required_skills) {
        this.required_skills = required_skills;
    }

    public Set<Parcours> getParcours() {
        return parcours;
    }

    public void setParcours(Set<Parcours> parcours) {
        this.parcours = parcours;
    }
}
