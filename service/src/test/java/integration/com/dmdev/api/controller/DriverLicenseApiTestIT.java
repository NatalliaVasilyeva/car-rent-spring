package integration.com.dmdev.api.controller;

import com.dmdev.domain.dto.driverlicense.response.DriverLicenseResponseDto;
import com.dmdev.domain.dto.userdetails.response.UserDetailsResponseDto;
import com.dmdev.service.DriverLicenseService;
import com.dmdev.service.UserService;
import com.dmdev.service.exception.NotFoundException;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.auth.WithMockCustomUser;
import integration.com.dmdev.utils.builder.TestDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static integration.com.dmdev.api.controller.DriverLicenseApiTestIT.MOCK_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
class DriverLicenseApiTestIT extends IntegrationBaseTest {

    private static final String ENDPOINT = "/driver-licenses";

    static final String MOCK_USERNAME = "admin@gmail.com";

    private final DriverLicenseService driverLicenseService;
    private final UserService userService;
    private final MockMvc mockMvc;
    private HttpHeaders commonHeaders = new HttpHeaders();

    @Test
    void shouldReturnNotFoundWithInvalidEndpoint() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/8974239878");

        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldReturnDriverLicenseByIdCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var savedUser = userService.create(userCreateRequestDTO);
        var expectedDriverLicense = savedUser.get().getDriverLicenseDto();

        assertExpectedIsSaved(expectedDriverLicense, expectedDriverLicense.getId());
    }

    @Test
    void shouldReturnDriverLicenseByUserIdCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var savedUser = userService.create(userCreateRequestDTO);
        var expectedDriverLicense = savedUser.get().getDriverLicenseDto();

        var uriBuilder = fromUriString(ENDPOINT + "/by-user-id");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("id", String.valueOf(savedUser.get().getId())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("driverLicense"))
                .andReturn();

        var responseDto = (DriverLicenseResponseDto) result.getModelAndView().getModel().get("driverLicense");

        assertThat(responseDto.getId()).isEqualTo(expectedDriverLicense.getId());
        assertThat(responseDto.getUserId()).isEqualTo(expectedDriverLicense.getUserId());
        assertThat(responseDto.getDriverLicenseNumber()).isEqualTo(expectedDriverLicense.getDriverLicenseNumber());
        assertThat(responseDto.getDriverLicenseIssueDate()).isEqualTo(expectedDriverLicense.getDriverLicenseIssueDate());
        assertThat(responseDto.getDriverLicenseExpiredDate()).isEqualTo(expectedDriverLicense.getDriverLicenseExpiredDate());
    }

    @Test
    void shouldReturnDriverLicenseByNumberCorrectly() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/by-number");
        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("number", "AAA*"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("driverLicensesPage"))
                .andReturn();
    }

    @Test
    void shouldReturnAllExpiredDriverLicenseCorrectly() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/expired");
        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("driverLicensesPage"))
                .andReturn();
    }


    @Test
    void shouldReturnAllDriverLicenses() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("driverLicensesPage"))
                .andReturn();

        var driverLicenses = ((Page<UserDetailsResponseDto>) result.getModelAndView().getModel().get("driverLicensesPage")).getContent();
        assertThat(driverLicenses).hasSize(2);
    }


    @Test
    void shouldUpdateDriverLicenseCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var savedUser = userService.create(userCreateRequestDTO);
        var expectedDriverLicense = savedUser.get().getDriverLicenseDto();

        assertExpectedIsSaved(expectedDriverLicense, expectedDriverLicense.getId());

        var driverLicenseUpdateRequestDTO = TestDtoBuilder.createDriverLicenseUpdateRequestDTO();
        var uriBuilder = fromUriString(ENDPOINT + "/" + expectedDriverLicense.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("driverLicenseNumber", driverLicenseUpdateRequestDTO.getDriverLicenseNumber())
                                .param("driverLicenseIssueDate", driverLicenseUpdateRequestDTO.getDriverLicenseIssueDate().toString())
                                .param("driverLicenseExpiredDate", driverLicenseUpdateRequestDTO.getDriverLicenseExpiredDate().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/users/" + expectedDriverLicense.getUserId()));
    }


    @Test
    void shouldReturn3xxOnDelete() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var savedUser = userService.create(userCreateRequestDTO);
        assertThat(savedUser).isPresent();
        var expectedDriverLicense = savedUser.get().getUserDetailsDto();

        mockMvc.perform(post(fromUriString(ENDPOINT + "/" + expectedDriverLicense.getId() + "/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> driverLicenseService.getById(expectedDriverLicense.getId()));

        assertEquals("404 NOT_FOUND \"Driver license with id 3 does not exist.\"", result.getMessage());
    }

    @Test
    void shouldReturn404onNoDelete() throws Exception {
        mockMvc.perform(post(fromUriString(ENDPOINT + "4782749/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNotFound());
    }

    private void assertExpectedIsSaved(DriverLicenseResponseDto expected, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("driverLicense"))
                .andReturn();

        var responseDto = (DriverLicenseResponseDto) result.getModelAndView().getModel().get("driverLicense");

        assertThat(responseDto.getId()).isEqualTo(expected.getId());
        assertThat(responseDto.getDriverLicenseNumber()).isEqualTo(expected.getDriverLicenseNumber());
        assertThat(responseDto.getDriverLicenseIssueDate()).isEqualTo(expected.getDriverLicenseIssueDate());
        assertThat(responseDto.getDriverLicenseExpiredDate()).isEqualTo(expected.getDriverLicenseExpiredDate());
    }
}