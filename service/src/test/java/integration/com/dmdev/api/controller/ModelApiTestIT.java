package integration.com.dmdev.api.controller;

import com.dmdev.domain.dto.model.ModelResponseDto;
import com.dmdev.service.BrandService;
import com.dmdev.service.CarService;
import com.dmdev.service.ModelService;
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
class ModelApiTestIT extends IntegrationBaseTest {

    private static final String ENDPOINT = "/models";
    static final String MOCK_USERNAME = "admin@gmail.com";

    private final CarService carService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final MockMvc mockMvc;
    private HttpHeaders commonHeaders = new HttpHeaders();

    @Test
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
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
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
    void shouldCreateModelCorrectly() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var uriBuilder = fromUriString(ENDPOINT);
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("brandId", modelCreateRequestDto.getBrandId().toString())
                                .param("name", modelCreateRequestDto.getName())
                                .param("transmission", modelCreateRequestDto.getTransmission().name())
                                .param("engineType", modelCreateRequestDto.getEngineType().name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/models"));
    }

    @Test
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
    void shouldReturnModelByIdCorrectly() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var saved = modelService.create(modelCreateRequestDto);
        var expected = saved.get();

        assertExpectedIsSaved(expected, expected.getId());
    }

    @Test
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
    void shouldReturnModelByBrandId() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var saved = modelService.create(modelCreateRequestDto);
        var expected = saved.get();

        var uriBuilder = fromUriString(ENDPOINT + "/brand-id");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("brandId", savedBrand.get().getId().toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("model"))
                .andReturn();

        var responseDto = (ModelResponseDto) result.getModelAndView().getModel().get("model");

        assertThat(responseDto.getId()).isEqualTo(expected.getId());
        assertThat(responseDto.getBrand()).isEqualTo(expected.getBrand());
    }


    @Test
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
    void shouldReturnAllModels() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("modelPage"))
                .andReturn();

        var cars = ((Page<ModelResponseDto>) result.getModelAndView().getModel().get("modelPage")).getContent();
        assertThat(cars).hasSize(2);
    }


    @Test
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
    void shouldUpdateModelCorrectly() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());

        var saved = modelService.create(modelCreateRequestDto);
        var expected = saved.get();

        assertExpectedIsSaved(expected, expected.getId());

        var modelUpdateRequestDto = TestDtoBuilder.createModelUpdateRequestDTO();
        var uriBuilder = fromUriString(ENDPOINT + "/" + expected.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", modelUpdateRequestDto.getName())
                                .param("transmission", modelUpdateRequestDto.getTransmission().name())
                                .param("engineType", modelUpdateRequestDto.getEngineType().name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT + "/" + expected.getId()));
    }

    @Test
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
    void shouldReturn3xxOnDelete() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var saved = modelService.create(modelCreateRequestDto);

        assertThat(saved).isPresent();

        mockMvc.perform(post(fromUriString(ENDPOINT + "/" + saved.get().getId() + "/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> modelService.getById(saved.get().getId()));

        assertEquals("404 NOT_FOUND \"Model with id 3 does not exist.\"", result.getMessage());
    }

    @Test
    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"CLIENT", "ADMIN"})
    void shouldReturn404onNoDelete() throws Exception {
        mockMvc.perform(post(fromUriString(ENDPOINT + "4782749/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNotFound());
    }

    private void assertExpectedIsSaved(ModelResponseDto expected, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("model"))
                .andReturn();

        var responseDto = (ModelResponseDto) result.getModelAndView().getModel().get("model");

        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getBrand()).isEqualTo(expected.getBrand());
        assertThat(responseDto.getName()).isEqualTo(expected.getName());
        assertThat(responseDto.getTransmission()).isEqualTo(expected.getTransmission());
        assertThat(responseDto.getEngineType()).isEqualTo(expected.getEngineType());
    }
}