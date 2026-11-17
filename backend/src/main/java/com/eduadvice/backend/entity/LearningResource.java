
package com.eduadvice.backend.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(
    name = "learning_resources",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_learning_resource_subject_url",
            columnNames = {"subject_id", "url"}
        )
    }
)
public class LearningResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2048)
    private String url;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false)
    private ResourceType resourceType;

    public LearningResource() {
    }

    public LearningResource(
            Subject subject,
            String title,
            String url,
            String description,
            ResourceType resourceType) {
        this.subject = subject;
        this.title = title;
        this.url = url;
        this.description = description;
        this.resourceType = resourceType;
    }

    // Getters and setters
    public void setId(Long id) {
    this.id = id;
}

    public Long getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }
}
