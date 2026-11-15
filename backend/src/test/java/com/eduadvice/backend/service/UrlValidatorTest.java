
package com.eduadvice.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlValidatorTest {

    private UrlValidator urlValidator;

    @BeforeEach
    void setUp() {
        urlValidator = new UrlValidator();
    }

    @Test
    void shouldAcceptValidHttpUrl() {
        assertDoesNotThrow(() ->
                urlValidator.validate("http://example.com")
        );
    }

    @Test
    void shouldAcceptValidHttpsUrl() {
        assertDoesNotThrow(() ->
                urlValidator.validate("https://example.com")
        );
    }

    @Test
    void shouldRejectNullUrl() {
        assertThrows(IllegalArgumentException.class, () ->
                urlValidator.validate(null)
        );
    }

    @Test
    void shouldRejectEmptyUrl() {
        assertThrows(IllegalArgumentException.class, () ->
                urlValidator.validate(" ")
        );
    }

    @Test
    void shouldRejectFtpUrl() {
        assertThrows(IllegalArgumentException.class, () ->
                urlValidator.validate("ftp://example.com")
        );
    }

    @Test
    void shouldRejectJavascriptUrl() {
        assertThrows(IllegalArgumentException.class, () ->
                urlValidator.validate("javascript:alert(1)")
        );
    }

    @Test
    void shouldRejectUrlWithoutHost() {
        assertThrows(IllegalArgumentException.class, () ->
                urlValidator.validate("https:///example.com")
        );
    }

    @Test
    void shouldRejectUrlContainingCredentials() {
        assertThrows(IllegalArgumentException.class, () ->
                urlValidator.validate("https://user:password@example.com")
        );
    }
}
