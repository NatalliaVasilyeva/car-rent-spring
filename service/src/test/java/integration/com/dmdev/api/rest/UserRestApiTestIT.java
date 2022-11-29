package integration.com.dmdev.api.rest;

import com.dmdev.domain.dto.filterdto.UserFilter;
import com.dmdev.domain.dto.user.response.UserResponseDto;
import com.dmdev.repository.UserRepository;
import com.dmdev.service.UserService;
import com.dmdev.utils.predicate.UserPredicateBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.auth.WithMockCustomUser;
import integration.com.dmdev.utils.builder.TestDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserRestApiTestIT extends IntegrationBaseTest {

    private static final String ENDPOINT = "/api/v1/users";

    static final String MOCK_USERNAME = "admin@gmail.com";

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserPredicateBuilder userPredicateBuilder;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private HttpHeaders commonHeaders = new HttpHeaders();

    @Test
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
    void shouldCreateUserCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT);
        String requestBody = objectMapper.writeValueAsString(userCreateRequestDTO);

        MvcResult result = mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        UserResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(userService.getById(responseDto.getId())).isPresent();
        assertThat(responseDto.getUsername()).isEqualTo(userCreateRequestDTO.getUsername());
        assertThat(responseDto.getEmail()).isEqualTo(userCreateRequestDTO.getEmail());
        assertThat(responseDto.getUserDetailsDto().getAddress()).isEqualTo(userCreateRequestDTO.getAddress());
    }

    @Test
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
    void shouldUpdateUserCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var savedUser = userService.create(userCreateRequestDTO);
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/" + savedUser.get().getId());

        var userUpdateRequestDTO = TestDtoBuilder.createUserUpdateRequestDTO();
        String requestBody = objectMapper.writeValueAsString(userUpdateRequestDTO);

        MvcResult result = mockMvc.perform(
                        put(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getUsername()).isEqualTo(userUpdateRequestDTO.getUsername());
        assertThat(responseDto.getEmail()).isEqualTo(userUpdateRequestDTO.getEmail());
    }

    @Test
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
    void shouldGetUserCorrectly() throws Exception {
        var existsUser = userService.getAll(UserFilter.builder().build(), 0, 100).stream().findFirst().orElseThrow();

        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/" + existsUser.getId());

        MvcResult result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getId()).isEqualTo(existsUser.getId());
        assertThat(responseDto.getUsername()).isEqualTo(existsUser.getUsername());
        assertThat(responseDto.getEmail()).isEqualTo(existsUser.getEmail());
    }

    @Test
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
    void shouldNotGetUserNotFound() throws Exception {
        final UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/9999999");

        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
    void shouldDeleteUserWithUserDetails() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var savedUser = userService.create(userCreateRequestDTO).get();

        mockMvc.perform(delete(fromUriString(ENDPOINT + "/" + savedUser.getId()).build().encode().toUri())
                        .headers(commonHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(savedUser.getId())).isFalse();
        assertThat(userRepository.existsById(savedUser.getUserDetailsDto().getId())).isFalse();
        assertThat(userRepository.existsById(savedUser.getDriverLicenseDto().getId())).isFalse();
    }

    @Test
    void shouldReturn401IfUnauthorized() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var savedUser = userService.create(userCreateRequestDTO).get();
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/" + savedUser.getId());

        mockMvc.perform(delete(uriBuilder.build().encode().toUri())
                        .headers(commonHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockCustomUser(username = "dummy", authorities = {"DUMMY"})
    void shouldReturn403IfMissingAuthorities() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var savedUser = userService.create(userCreateRequestDTO).get();
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/" + savedUser.getId());

        mockMvc.perform(delete(uriBuilder.build().encode().toUri())
                        .headers(commonHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
    }
}