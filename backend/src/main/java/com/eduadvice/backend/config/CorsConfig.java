package com.eduadvice.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * CORS : autorise le frontend (servi depuis un autre domaine, ex. Vercel)
 * à appeler l'API depuis le navigateur.
 *
 * Les origines autorisées viennent de la variable d'environnement
 * CORS_ALLOWED_ORIGINS (voir application.yaml) : le même code tourne
 * partout, seule la configuration change selon l'environnement.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String[] allowedOrigins;

    public CorsConfig(@Value("${app.cors.allowed-origins}") String[] allowedOrigins) {
        // Échouer au démarrage plutôt que d'ouvrir l'API à tout le monde.
        if (Arrays.asList(allowedOrigins).contains("*")) {
            throw new IllegalStateException(
                    "CORS_ALLOWED_ORIGINS ne doit pas contenir '*' : listez explicitement les origines autorisées.");
        }
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                // Uniquement les méthodes que l'API expose réellement.
                .allowedMethods("GET", "POST")
                .allowedHeaders("Accept", "Content-Type")
                // Pas de cookies ni d'authentification envoyés par le navigateur.
                .allowCredentials(false)
                // Le navigateur garde la réponse "preflight" en cache 1 h.
                .maxAge(3600);
    }
}
