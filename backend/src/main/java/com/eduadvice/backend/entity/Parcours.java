package com.eduadvice.backend.entity;

import jakarta.persistence.*;
/**
 * Configuration class responsible for Hibernate database schema generation.
 * This class configures Hibernate to automatically create or update tables
 */
@Entity
public class Parcours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(nullable = false)
    private String name;

    @Basic
    @Column(nullable = false)
    private String level;

    @Basic
    @Column(nullable = false)
    private String diploma;

    private String mention;

    @Basic
    @Column(nullable = false)
    private String establishment;

    @Column(columnDefinition ="TEXT")
    private String description;

    public Parcours() {
    }

    public Parcours(String name, String level, String diploma, String mention, String establishment, String description) {
        this.name = name;
        this.level = level;
        this.diploma = diploma;
        this.mention = mention;
        this.establishment = establishment;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public String getDiploma() {
        return diploma;
    }

    public String getMention() {
        return mention;
    }

    public String getEstablishment() {
        return establishment;
    }

    public String getDescription() {
        return description;
    }
}
