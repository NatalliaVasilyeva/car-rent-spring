package integration.com.dmdev.api.controller;

import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.auth.WithMockCustomUser;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class WelcomeApiTestIT extends IntegrationBaseTest {

    static final String MOCK_USERNAME = "admin@gmail.com";
    private final MockMvc mockMvc;
    private final HttpHeaders commonHeaders = new HttpHeaders();

    @Test
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
    void shouldRedirectToCorrectPage() throws Exception {
        final UriComponentsBuilder uriBuilder = fromUriString("/welcome");

        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is2xxSuccessful());
    }
}