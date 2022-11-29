package integration.com.dmdev.api.controller;

import com.dmdev.domain.dto.brand.response.BrandResponseDto;
import com.dmdev.domain.dto.car.CarResponseDto;
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

import static integration.com.dmdev.api.controller.CarApiTestIT.MOCK_USERNAME;
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
class CarApiTestIT extends IntegrationBaseTest {

    private static final String ENDPOINT = "/cars";
    static final String MOCK_USERNAME = "admin@gmail.com";

    private final CarService carService;
    private final BrandService brandService;
    private final ModelService modelService;
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
    void shouldCreateCarCorrectly() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var uriBuilder = fromUriString(ENDPOINT);
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("brandId", carCreateRequestDto.getBrandId().toString())
                                .param("modelId", carCreateRequestDto.getModelId().toString())
                                .param("categoryId", carCreateRequestDto.getCategoryId().toString())
                                .param("color", carCreateRequestDto.getColor().toString())
                                .param("yearOfProduction", carCreateRequestDto.getYearOfProduction().toString())
                                .param("number", carCreateRequestDto.getNumber())
                                .param("vin", carCreateRequestDto.getVin())
                                .param("isRepaired", carCreateRequestDto.getIsRepaired().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/cars"));
    }

    @Test
    void shouldReturnCarByIdCorrectly() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var saved = carService.create(carCreateRequestDto);
        var expected = saved.get();

        assertExpectedIsSaved(expected, expected.getId());
    }

    @Test
    
    void shouldReturnCarByNumberCorrectly() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var saved = carService.create(carCreateRequestDto);
        var expected = saved.get();

        var uriBuilder = fromUriString(ENDPOINT + "/by-number");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("number", saved.get().getNumber()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("car"))
                .andReturn();

        var responseDto = (CarResponseDto) result.getModelAndView().getModel().get("car");

        assertThat(responseDto.getId()).isEqualTo(expected.getId());
        assertThat(responseDto.getNumber()).isEqualTo(expected.getNumber());
    }

    @Test
    void shouldReturnAllWithAccidents() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        carService.create(carCreateRequestDto);

        var uriBuilder = fromUriString(ENDPOINT + "/with-accidents");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("carPage"))
                .andReturn();

        var cars = ((Page<CarResponseDto>) result.getModelAndView().getModel().get("carPage")).getContent();

        assertThat(cars).hasSize(2);
    }

    @Test
    void shouldReturnAllWithoutAccidents() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        carService.create(carCreateRequestDto);

        var uriBuilder = fromUriString(ENDPOINT + "/without-accidents");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("carPage"))
                .andReturn();

        var cars = ((Page<BrandResponseDto>) result.getModelAndView().getModel().get("carPage")).getContent();

        assertThat(cars).hasSize(1);
    }

    @Test
    void shouldReturnAllUnderRepair() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        carService.create(carCreateRequestDto);

        var uriBuilder = fromUriString(ENDPOINT + "/under-repair");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("carPage"))
                .andReturn();

        var cars = ((Page<BrandResponseDto>) result.getModelAndView().getModel().get("carPage")).getContent();

        assertThat(cars).hasSize(0);
    }

    @Test
    void shouldReturnAllCars() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("carPage"))
                .andReturn();

        var cars = ((Page<CarResponseDto>) result.getModelAndView().getModel().get("carPage")).getContent();
        assertThat(cars).hasSize(2);
    }


    @Test
    void shouldUpdateModelCorrectly() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());

        var saved = carService.create(carCreateRequestDto);
        var expected = saved.get();

        assertExpectedIsSaved(expected, expected.getId());

        var carUpdateRequestDto = TestDtoBuilder.createCarUpdateRequestDTO(savedBrand.get().getId());
        var uriBuilder = fromUriString(ENDPOINT + "/" + expected.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("modelId", carUpdateRequestDto.getModelId().toString())
                                .param("categoryId", carUpdateRequestDto.getCategoryId().toString())
                                .param("color", carUpdateRequestDto.getColor().name())
                                .param("yearOfProduction", carUpdateRequestDto.getYearOfProduction().toString())
                                .param("number", carUpdateRequestDto.getNumber())
                                .param("isRepaired", carUpdateRequestDto.getIsRepaired().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT + "/" + expected.getId()));
    }

    @Test
    void shouldReturn3xxOnDelete() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var saved = carService.create(carCreateRequestDto);

        assertThat(saved).isPresent();

        mockMvc.perform(post(fromUriString(ENDPOINT + "/" + saved.get().getId() + "/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> carService.getById(saved.get().getId()));

        assertEquals("404 NOT_FOUND \"Car with id 3 does not exist.\"", result.getMessage());
    }

    @Test
    void shouldReturn404onNoDelete() throws Exception {
        mockMvc.perform(post(fromUriString(ENDPOINT + "4782749/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNotFound());
    }

    private void assertExpectedIsSaved(CarResponseDto expected, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("car"))
                .andReturn();

        var responseDto = (CarResponseDto) result.getModelAndView().getModel().get("car");

        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getBrand()).isEqualTo(expected.getBrand());
        assertThat(responseDto.getNumber()).isEqualTo(expected.getNumber());
        assertThat(responseDto.getCategory()).isEqualTo(expected.getCategory());
        assertThat(responseDto.getYearOfProduction()).isEqualTo(expected.getYearOfProduction());
        assertThat(responseDto.getPrice()).isEqualTo(expected.getPrice());
        assertThat(responseDto.getVin()).isEqualTo(expected.getVin());
        assertThat(responseDto.getColor()).isEqualTo(expected.getColor());
        assertThat(responseDto.getModel()).isEqualTo(expected.getModel());
    }
}