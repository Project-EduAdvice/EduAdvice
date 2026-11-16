
package com.eduadvice.backend.service;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Set;

@Component
public class UrlValidator {

    private static final Set<String> ALLOWED_SCHEMES =
            Set.of("http", "https");

    public void validate(String url) {

        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL cannot be empty");
        }

        try {
            URI uri = new URI(url.trim());

            if (!uri.isAbsolute()) {
                throw new IllegalArgumentException("URL must be absolute");
            }

            String scheme = uri.getScheme().toLowerCase(Locale.ROOT);

            if (!ALLOWED_SCHEMES.contains(scheme)) {
                throw new IllegalArgumentException(
                        "Only HTTP and HTTPS URLs are allowed"
                );
            }

            if (uri.getHost() == null || uri.getHost().isBlank()) {
                throw new IllegalArgumentException("URL must have a valid host");
            }

            if (uri.getRawUserInfo() != null) {
                throw new IllegalArgumentException(
                        "URLs containing credentials are not allowed"
                );
            }

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL format");
        }
    }
}
