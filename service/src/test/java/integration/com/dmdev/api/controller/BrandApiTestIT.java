package integration.com.dmdev.api.controller;

import com.dmdev.domain.dto.brand.response.BrandResponseDto;
import com.dmdev.service.BrandService;
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

import static integration.com.dmdev.api.controller.BrandApiTestIT.MOCK_USERNAME;
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
@WithMockCustomUser(username = CategoryApiTestIT.MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
class BrandApiTestIT extends IntegrationBaseTest {

    private static final String ENDPOINT = "/brands";
    static final String MOCK_USERNAME = "admin@gmail.com";

    private final BrandService brandService;
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
    void shouldCreateBrandCorrectly() throws Exception {
        var brandCreateEditRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var uriBuilder = fromUriString(ENDPOINT);
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", brandCreateEditRequestDto.getName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/brands"));
    }

    @Test
    void shouldReturnBrandByIdCorrectly() throws Exception {
        var brandCreateEditRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var saved = brandService.create(brandCreateEditRequestDto);
        var expected = saved.get();

        assertExpectedIsSaved(expected, expected.getId());
    }

    @Test
    void shouldReturnBrandByNameCorrectly() throws Exception {
        var brandCreateEditRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var saved = brandService.create(brandCreateEditRequestDto);
        var expected = saved.get();

        var uriBuilder = fromUriString(ENDPOINT + "/by-name");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", "toyota"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("brandPage"))
                .andReturn();

        var brands = ((Page<BrandResponseDto>) result.getModelAndView().getModel().get("brandPage")).getContent();

        assertThat(brands).hasSize(1);
        assertThat(brands.get(0).getId()).isEqualTo(expected.getId());
        assertThat(brands.get(0).getName()).isEqualTo(expected.getName());
    }

    @Test
    void shouldReturnBrandByNamesCorrectly() throws Exception {
        var brandCreateEditRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var saved = brandService.create(brandCreateEditRequestDto);
        var expected = saved.get();

        var uriBuilder = fromUriString(ENDPOINT + "/by-names");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("names", "toyota", "reno"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("brandPage"))
                .andReturn();

        var brands = ((Page<BrandResponseDto>) result.getModelAndView().getModel().get("brandPage")).getContent();

        assertThat(brands).hasSize(1);
        assertThat(brands.get(0).getId()).isEqualTo(expected.getId());
        assertThat(brands.get(0).getName()).isEqualTo(expected.getName());
    }

    @Test
    void shouldReturnAllBrandsAllView() throws Exception {
        var brandCreateEditRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        brandService.create(brandCreateEditRequestDto);

        var uriBuilder = fromUriString(ENDPOINT + "/all-full-view");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("brandFullViewPage"))
                .andReturn();

        var brands = ((Page<BrandResponseDto>) result.getModelAndView().getModel().get("brandFullViewPage")).getContent();

        assertThat(brands).hasSize(2);
    }

    @Test
    void shouldReturnAllBrands() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("brandPage"))
                .andReturn();

        var brands = ((Page<BrandResponseDto>) result.getModelAndView().getModel().get("brandPage")).getContent();
        assertThat(brands).hasSize(2);
    }


    @Test
    void shouldUpdateBrandCorrectly() throws Exception {
        var brandCreateEditRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var saved = brandService.create(brandCreateEditRequestDto);
        var expected = saved.get();

        assertExpectedIsSaved(expected, expected.getId());

        var brandUpdateRequestDTO = TestDtoBuilder.createBrandUpdateRequestDTO();
        var uriBuilder = fromUriString(ENDPOINT + "/" + expected.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", brandUpdateRequestDTO.getName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT + "/" + expected.getId()));
    }

    @Test
    void shouldReturn3xxOnDelete() throws Exception {
        var brandCreateEditRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var saved = brandService.create(brandCreateEditRequestDto);

        assertThat(saved).isPresent();

        mockMvc.perform(post(fromUriString(ENDPOINT + "/" + saved.get().getId() + "/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> brandService.getById(saved.get().getId()));

        assertEquals("404 NOT_FOUND \"Brand with id 3 does not exist.\"", result.getMessage());
    }

    @Test
    void shouldReturn404onNoDelete() throws Exception {
        mockMvc.perform(post(fromUriString(ENDPOINT + "4782749/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNotFound());
    }

    private void assertExpectedIsSaved(BrandResponseDto expected, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("brand"))
                .andReturn();

        var responseDto = (BrandResponseDto) result.getModelAndView().getModel().get("brand");

        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getName()).isEqualTo(expected.getName());
    }
}