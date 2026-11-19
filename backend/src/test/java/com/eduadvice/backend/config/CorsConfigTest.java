package com.eduadvice.backend.config;

import com.eduadvice.backend.controller.ParcoursController;
import com.eduadvice.backend.service.ParcoursService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Vérifie la politique CORS en simulant la requête "preflight" (OPTIONS)
 * qu'envoie un navigateur avant un appel cross-origin.
 */
@WebMvcTest(ParcoursController.class)
@TestPropertySource(properties = "app.cors.allowed-origins=https://eduadvice.example")
class CorsConfigTest {

    private static final String ALLOWED_ORIGIN = "https://eduadvice.example";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ParcoursService parcoursService;

    @Test
    void preflight_fromAllowedOrigin_isAccepted() throws Exception {
        mockMvc.perform(options("/api/parcours")
                        .header("Origin", ALLOWED_ORIGIN)
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", ALLOWED_ORIGIN));
    }

    @Test
    void preflight_fromUnknownOrigin_isRejected() throws Exception {
        mockMvc.perform(options("/api/parcours")
                        .header("Origin", "https://site-malveillant.example")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    void preflight_withMethodNotExposed_isRejected() throws Exception {
        mockMvc.perform(options("/api/parcours")
                        .header("Origin", ALLOWED_ORIGIN)
                        .header("Access-Control-Request-Method", "DELETE"))
                .andExpect(status().isForbidden());
    }

    @Test
    void wildcardOrigin_isRefusedAtStartup() {
        assertThatThrownBy(() -> new CorsConfig(new String[]{"*"}))
                .isInstanceOf(IllegalStateException.class);
    }
}
