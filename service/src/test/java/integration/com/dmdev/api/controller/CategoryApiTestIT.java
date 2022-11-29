package integration.com.dmdev.api.controller;

import com.dmdev.domain.dto.category.response.CategoryResponseDto;
import com.dmdev.service.CategoryService;
import com.dmdev.service.exception.NotFoundException;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.auth.WithMockCustomUser;
import integration.com.dmdev.utils.builder.TestDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static integration.com.dmdev.api.controller.CategoryApiTestIT.MOCK_USERNAME;
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
class CategoryApiTestIT extends IntegrationBaseTest {

    private static final String ENDPOINT = "/categories";

    static final String MOCK_USERNAME = "admin@gmail.com";

    private final CategoryService categoryService;
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
    void shouldCreateCategoryCorrectly() throws Exception {
        var categoryCreateEditRequestDto = TestDtoBuilder.createCategoryCreateEditRequestDto();
        var uriBuilder = fromUriString(ENDPOINT);
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", categoryCreateEditRequestDto.getName())
                                .param("price", categoryCreateEditRequestDto.getPrice().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/categories"));
    }

    @Test
    void shouldReturnCategoryByIdCorrectly() throws Exception {
        var categoryCreateEditRequestDto = TestDtoBuilder.createCategoryCreateEditRequestDto();
        var saved = categoryService.create(categoryCreateEditRequestDto);
        var expected = saved.get();

        assertExpectedIsSaved(expected, expected.getId());
    }

    @Test
    void shouldReturnCategoriesByPriceCorrectly() throws Exception {
        var categoryCreateEditRequestDto = TestDtoBuilder.createCategoryCreateEditRequestDto();
        var saved = categoryService.create(categoryCreateEditRequestDto);
        var expected = saved.get();

        var uriBuilder = fromUriString(ENDPOINT + "/by-price");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("price", "120")
                                .param("type", "equals"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("categories"))
                .andReturn();

        var categories = (List<CategoryResponseDto>) result.getModelAndView().getModel().get("categories");

        assertThat(categories).hasSize(1);
        assertThat(categories.get(0).getId()).isEqualTo(expected.getId());
        assertThat(categories.get(0).getName()).isEqualTo(expected.getName());
        assertThat(categories.get(0).getPrice()).isEqualTo(expected.getPrice());
    }

    @Test
    void shouldReturnAllCategories() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("categories"))
                .andReturn();

        var categories = (List<CategoryResponseDto>) result.getModelAndView().getModel().get("categories");
        assertThat(categories).hasSize(2);
    }


    @Test
    void shouldUpdateCategoryCorrectly() throws Exception {
        var categoryCreateEditRequestDto = TestDtoBuilder.createCategoryCreateEditRequestDto();
        var saved = categoryService.create(categoryCreateEditRequestDto);
        var expected = saved.get();

        assertExpectedIsSaved(expected, expected.getId());

        var categoryUpdateRequestDto = TestDtoBuilder.createCategoryUpdateRequestDto();
        var uriBuilder = fromUriString(ENDPOINT + "/" + expected.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", categoryUpdateRequestDto.getName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT + "/" + expected.getId()));
    }

    @Test
    void shouldReturn3xxOnDelete() throws Exception {
        var categoryCreateEditRequestDto = TestDtoBuilder.createCategoryCreateEditRequestDto();
        var saved = categoryService.create(categoryCreateEditRequestDto);

        assertThat(saved).isPresent();

        mockMvc.perform(post(fromUriString(ENDPOINT + "/" + saved.get().getId() + "/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> categoryService.getById(saved.get().getId()));

        assertEquals("404 NOT_FOUND \"Category with id 3 does not exist.\"", result.getMessage());
    }

    @Test
    void shouldReturn404onNoDelete() throws Exception {
        mockMvc.perform(post(fromUriString(ENDPOINT + "4782749/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNotFound());
    }

    private void assertExpectedIsSaved(CategoryResponseDto expected, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("category"))
                .andReturn();

        var responseDto = (CategoryResponseDto) result.getModelAndView().getModel().get("category");

        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getName()).isEqualTo(expected.getName());
    }
}