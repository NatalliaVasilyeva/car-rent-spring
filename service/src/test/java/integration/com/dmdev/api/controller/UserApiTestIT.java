package integration.com.dmdev.api.controller;

import com.dmdev.domain.dto.user.response.UserResponseDto;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static integration.com.dmdev.api.controller.UserApiTestIT.MOCK_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
class UserApiTestIT extends IntegrationBaseTest {

    private static final String ENDPOINT = "/users";

    static final String MOCK_USERNAME = "admin@gmail.com";

    private final UserService userService;
    private final MockMvc mockMvc;
    private final HttpHeaders commonHeaders = new HttpHeaders();

    @Test
    void shouldReturnNotFoundWithInvalidEndpoint() throws Exception {
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/8974239878");

        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldLoginCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        userService.create(userCreateRequestDTO).get();

        UriComponentsBuilder uriBuilder = fromUriString("/login");

        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("email", "test@gmail.com")
                                .param("password", "Testtesttest1"))
                .andExpect(redirectedUrl("/welcome"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldLoginCorrectlyLoginForm() throws Exception {
        RequestBuilder requestBuilder = formLogin("/login")
                .user("email", "client@gmail.com")
                .password("TestTest1234!");
        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/welcome"));
    }

    @Test
    void shouldLogoutCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        userService.create(userCreateRequestDTO).get();

        UriComponentsBuilder uriLoginBuilder = fromUriString("/login");

        mockMvc.perform(
                        post(uriLoginBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("email", "test@gmail.com")
                                .param("password", "Testtesttest1"))
                .andExpect(redirectedUrl("/welcome"))
                .andExpect(status().is3xxRedirection());

        UriComponentsBuilder uriLogoutBuilder = fromUriString("/logout");

        mockMvc.perform(
                        post(uriLogoutBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("email", "test@gmail.com")
                                .param("password", "Testtesttest1"))
                .andExpect(redirectedUrl("/welcome"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldCreateUserCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/sing-up");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("email", userCreateRequestDTO.getEmail())
                                .param("username", userCreateRequestDTO.getUsername())
                                .param("password", userCreateRequestDTO.getPassword())
                                .param("name", userCreateRequestDTO.getName())
                                .param("surname", userCreateRequestDTO.getSurname())
                                .param("address", userCreateRequestDTO.getAddress())
                                .param("phone", userCreateRequestDTO.getPhone())
                                .param("birthday", userCreateRequestDTO.getBirthday().toString())
                                .param("driverLicenseNumber", userCreateRequestDTO.getDriverLicenseNumber())
                                .param("driverLicenseIssueDate", userCreateRequestDTO.getDriverLicenseIssueDate().toString())
                                .param("driverLicenseExpiredDate", userCreateRequestDTO.getDriverLicenseExpiredDate().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/welcome"));
    }

    @Test
    void shouldReturnUserByIdCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();

        var saved = userService.create(userCreateRequestDTO);

        var expected = saved.get();
        assertExpectedIsSaved(expected, expected.getId());
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT);
        MvcResult result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usersPage"))
                .andReturn();

        List<UserResponseDto> users = ((Page<UserResponseDto>) result.getModelAndView().getModel().get("usersPage")).getContent();
        assertThat(users).hasSize(2);
    }

    @Test
    void shouldReturn200IfUserLoginCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();

        var saved = userService.create(userCreateRequestDTO);
        var expected = saved.get();
        assertExpectedIsSaved(expected, expected.getId());

        mockMvc.perform(post(fromUriString("/login").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("email", userCreateRequestDTO.getEmail())
                        .param("password", userCreateRequestDTO.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/welcome"));
    }

    @Test
    void shouldReturn200IfUserLogoutCorrectly() throws Exception {

        mockMvc.perform(post(fromUriString("/logout").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/welcome"));
    }

    @Test
    void shouldUpdateUserCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();

        var saved = userService.create(userCreateRequestDTO);
        var expected = saved.get();
        assertExpectedIsSaved(expected, expected.getId());

        var userUpdateRequestDTO = TestDtoBuilder.createUserUpdateRequestDTO();
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/" + expected.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("email", userUpdateRequestDTO.getEmail())
                                .param("username", userUpdateRequestDTO.getUsername())
                                .param("role", userUpdateRequestDTO.getRole().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT + "/" + expected.getId()));
    }

    @Test
    void shouldChangePasswordCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();

        var saved = userService.create(userCreateRequestDTO);
        var expected = saved.get();
        assertExpectedIsSaved(expected, expected.getId());

        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/" + expected.getId() + "/change-password");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("oldPassword", userCreateRequestDTO.getPassword())
                                .param("newPassword", "test1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT + "/" + expected.getId()));
    }

    @Test
    void shouldReturn3xxOnDelete() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();

        var saved = userService.create(userCreateRequestDTO);

        assertThat(saved).isPresent();

        mockMvc.perform(post(fromUriString(ENDPOINT + "/" + saved.get().getId() + "/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> userService.getById(saved.get().getId()));

        assertEquals("404 NOT_FOUND \"User with id 3 does not exist.\"", result.getMessage());
    }

    @Test
    void shouldReturn404onNoDelete() throws Exception {
        mockMvc.perform(post(fromUriString(ENDPOINT + "4782749/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "dummy", authorities = {"DUMMY"})
    void mustReturn403IfWrongAuthorityUpdate() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();

        var saved = userService.create(userCreateRequestDTO);
        var expected = saved.get();

        var userUpdateRequestDTO = TestDtoBuilder.createUserUpdateRequestDTO();
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/" + expected.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("email", userUpdateRequestDTO.getEmail())
                                .param("username", userUpdateRequestDTO.getUsername())
                                .param("role", userUpdateRequestDTO.getRole().toString()))
                .andExpect(status().isForbidden());
    }

    private void assertExpectedIsSaved(UserResponseDto expected, Long id) throws Exception {
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/" + id);
        MvcResult result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andReturn();

        UserResponseDto responseDto = (UserResponseDto) result.getModelAndView().getModel().get("user");

        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getUsername()).isEqualTo(expected.getUsername());
        assertThat(responseDto.getEmail()).isEqualTo(expected.getEmail());
        assertThat(responseDto.getRole()).isEqualTo(expected.getRole());

        assertThat(responseDto.getUserDetailsDto().getId()).isEqualTo(expected.getUserDetailsDto().getId());
        assertThat(responseDto.getUserDetailsDto().getName()).isEqualTo(expected.getUserDetailsDto().getName());
        assertThat(responseDto.getUserDetailsDto().getSurname()).isEqualTo(expected.getUserDetailsDto().getSurname());
        assertThat(responseDto.getUserDetailsDto().getAddress()).isEqualTo(expected.getUserDetailsDto().getAddress());
        assertThat(responseDto.getUserDetailsDto().getPhone()).isEqualTo(expected.getUserDetailsDto().getPhone());
        assertThat(responseDto.getUserDetailsDto().getBirthday()).isEqualTo(expected.getUserDetailsDto().getBirthday());

        assertThat(responseDto.getDriverLicenseDto().getId()).isEqualTo(expected.getDriverLicenseDto().getId());
        assertThat(responseDto.getDriverLicenseDto().getDriverLicenseNumber()).isEqualTo(expected.getDriverLicenseDto().getDriverLicenseNumber());
        assertThat(responseDto.getDriverLicenseDto().getDriverLicenseIssueDate()).isEqualTo(expected.getDriverLicenseDto().getDriverLicenseIssueDate());
        assertThat(responseDto.getDriverLicenseDto().getDriverLicenseExpiredDate()).isEqualTo(expected.getDriverLicenseDto().getDriverLicenseExpiredDate());
    }
}