package integration.com.dmdev.api;

import com.dmdev.domain.dto.userdetails.response.UserDetailsResponseDto;
import com.dmdev.service.UserDetailsService;
import com.dmdev.service.UserService;
import com.dmdev.service.exception.NotFoundException;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.TestDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

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
class UserDetailsApiTestIT extends IntegrationBaseTest {

    private static final String ENDPOINT = "/user-details";

    private final UserDetailsService userDetailsService;
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
    void shouldReturnUserDetailsByIdCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var savedUser = userService.create(userCreateRequestDTO);
        var expectedUserDetails = savedUser.get().getUserDetailsDto();

        assertExpectedIsSaved(expectedUserDetails, expectedUserDetails.getId());
    }

    @Test
    void shouldReturnUserDetailsByUserIdCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var savedUser = userService.create(userCreateRequestDTO);
        var expectedUserDetails = savedUser.get().getUserDetailsDto();

        var uriBuilder = fromUriString(ENDPOINT + "/by-user-id");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("id", String.valueOf(savedUser.get().getId())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userDetails"))
                .andReturn();

        var responseDto = (UserDetailsResponseDto) result.getModelAndView().getModel().get("userDetails");

        assertThat(responseDto.getId()).isEqualTo(expectedUserDetails.getId());
        assertThat(responseDto.getName()).isEqualTo(expectedUserDetails.getName());
        assertThat(responseDto.getSurname()).isEqualTo(expectedUserDetails.getSurname());
        assertThat(responseDto.getAddress()).isEqualTo(expectedUserDetails.getAddress());
        assertThat(responseDto.getPhone()).isEqualTo(expectedUserDetails.getPhone());
        assertThat(responseDto.getBirthday()).isEqualTo(expectedUserDetails.getBirthday());
    }

    @Test
    void shouldReturnUserDetailsByNameAndSurnameCorrectly() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/by-name-surname");
        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", "v")
                                .param("surname", "k"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usersDetailsPage"))
                .andReturn();
    }

    @Test
    void shouldReturnUserDetailsByRegistrationDateCorrectly() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/by-registration-date");
        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("registrationDate", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usersDetailsPage"))
                .andReturn();
    }

    @Test
    void shouldReturnUserDetailsByRegistrationDatesCorrectly() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/by-registration-dates");
        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("from", LocalDate.now().toString())
                                .param("to", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usersDetailsPage"))
                .andReturn();
    }

    @Test
    void shouldReturnAllUsersDetails() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usersDetailsPage"))
                .andReturn();

        var userDetails = ((Page<UserDetailsResponseDto>) result.getModelAndView().getModel().get("usersDetailsPage")).getContent();
        assertThat(userDetails).hasSize(2);
    }


    @Test
    void shouldUpdateUserCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var savedUser = userService.create(userCreateRequestDTO);
        var expectedUserDetails = savedUser.get().getUserDetailsDto();

        assertExpectedIsSaved(expectedUserDetails, expectedUserDetails.getId());

        var userDetailsUpdateRequestDTO = TestDtoBuilder.createUserDetailsUpdateRequestDTO();
        var uriBuilder = fromUriString(ENDPOINT + "/" + expectedUserDetails.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", userDetailsUpdateRequestDTO.getName())
                                .param("surname", userDetailsUpdateRequestDTO.getSurname())
                                .param("address", userDetailsUpdateRequestDTO.getAddress())
                                .param("phone", userDetailsUpdateRequestDTO.getPhone()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/users/" + expectedUserDetails.getId()));
    }


    @Test
    void shouldReturn3xxOnDelete() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var savedUser = userService.create(userCreateRequestDTO);
        assertThat(savedUser).isPresent();
        var expectedUserDetails = savedUser.get().getUserDetailsDto();

        mockMvc.perform(post(fromUriString(ENDPOINT + "/" + expectedUserDetails.getId() + "/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> userDetailsService.getById(expectedUserDetails.getId()));

        assertEquals("404 NOT_FOUND \"User details with id 3 does not exist.\"", result.getMessage());
    }

    @Test
    void shouldReturn404onNoDelete() throws Exception {
        mockMvc.perform(post(fromUriString(ENDPOINT + "4782749/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNotFound());
    }

    private void assertExpectedIsSaved(UserDetailsResponseDto expected, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userDetails"))
                .andReturn();

        var responseDto = (UserDetailsResponseDto) result.getModelAndView().getModel().get("userDetails");

        assertThat(responseDto.getId()).isEqualTo(expected.getId());
        assertThat(responseDto.getName()).isEqualTo(expected.getName());
        assertThat(responseDto.getSurname()).isEqualTo(expected.getSurname());
        assertThat(responseDto.getAddress()).isEqualTo(expected.getAddress());
        assertThat(responseDto.getPhone()).isEqualTo(expected.getPhone());
        assertThat(responseDto.getBirthday()).isEqualTo(expected.getBirthday());
    }
}