package integration.com.dmdev.api;

import integration.com.dmdev.IntegrationBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

public class WelcomeApiTestIT extends IntegrationBaseTest  {

    @Test
    void shouldRedirectToCorrectPage() throws Exception {
        final UriComponentsBuilder uriBuilder = fromUriString("/api/v1/welcome");

        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is2xxSuccessful());
    }
}