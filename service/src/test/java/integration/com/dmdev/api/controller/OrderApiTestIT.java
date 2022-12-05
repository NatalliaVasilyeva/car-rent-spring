package integration.com.dmdev.api.controller;

import com.dmdev.domain.dto.order.OrderResponseDto;
import com.dmdev.domain.model.OrderStatus;
import com.dmdev.service.BrandService;
import com.dmdev.service.CarService;
import com.dmdev.service.ModelService;
import com.dmdev.service.OrderService;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

import static integration.com.dmdev.api.controller.OrderApiTestIT.MOCK_USERNAME;
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
class OrderApiTestIT extends IntegrationBaseTest {

    private static final String ENDPOINT = "/orders";

    static final String MOCK_USERNAME = "admin@gmail.com";

    private final UserService userService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final CarService carService;
    private final OrderService orderService;
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
    void shouldCreateOrderCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var actualUser = userService.create(userCreateRequestDTO);
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);

        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT);
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("userId", orderCreateRequestDTO.getUserId().toString())
                                .param("carId", orderCreateRequestDTO.getCarId().toString())
                                .param("passport", orderCreateRequestDTO.getPassport())
                                .param("insurance", orderCreateRequestDTO.getInsurance().toString())
                                .param("startRentalDate", orderCreateRequestDTO.getStartRentalDate().toString())
                                .param("endRentalDate", orderCreateRequestDTO.getEndRentalDate().toString()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldNotCreateOrderDueToUnavailableCarCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var actualUser = userService.create(userCreateRequestDTO);
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);

        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());
        orderService.create(orderCreateRequestDTO);

        var orderCreateRequestDTOSame = TestDtoBuilder.createOrderRequestDtoWithNecessaryData(actualUser.get().getId(), actualCar.get().getId(), orderCreateRequestDTO.getStartRentalDate(), orderCreateRequestDTO.getEndRentalDate());

        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT);
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("userId", orderCreateRequestDTOSame.getUserId().toString())
                                .param("carId", orderCreateRequestDTOSame.getCarId().toString())
                                .param("passport", orderCreateRequestDTOSame.getPassport())
                                .param("insurance", orderCreateRequestDTOSame.getInsurance().toString())
                                .param("startRentalDate", orderCreateRequestDTOSame.getStartRentalDate().toString())
                                .param("endRentalDate", orderCreateRequestDTOSame.getEndRentalDate().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/cars"));
    }

    @Test
    void shouldReturnOrderByIdCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var actualUser = userService.create(userCreateRequestDTO);
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);

        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());
        var saved = orderService.create(orderCreateRequestDTO);

        var expected = saved.get();
        assertExpectedIsSaved(expected, expected.getId());
    }

    @Test
    void shouldReturnAllOrders() throws Exception {
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT);
        MvcResult result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ordersPage"))
                .andReturn();

        List<OrderResponseDto> orders = ((Page<OrderResponseDto>) result.getModelAndView().getModel().get("ordersPage")).getContent();
        assertThat(orders).hasSize(2);
    }

    @Test
    void shouldReturnAllOrdersByStatus() throws Exception {
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/by-status");
        MvcResult result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("userFirstName", "")
                                .param("userLastName", "")
                                .param("carNumber", "")
                                .param("orderStatus", "")
                                .param("sum", "")
                                .param("status", OrderStatus.DECLINED.name()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ordersPage"))
                .andReturn();

        List<OrderResponseDto> orders = ((Page<OrderResponseDto>) result.getModelAndView().getModel().get("ordersPage")).getContent();
        assertThat(orders).isEmpty();
    }

    @Test
    void shouldReturnAllOrdersByUserId() throws Exception {
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/user/1");
        MvcResult result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("status", OrderStatus.DECLINED.name())
                                .param("sum", BigDecimal.ZERO.toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ordersPage"))
                .andReturn();

        List<OrderResponseDto> orders = ((Page<OrderResponseDto>) result.getModelAndView().getModel().get("ordersPage")).getContent();
        assertThat(orders).isEmpty();
    }

    @Test
    void shouldUpdateOrderCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var actualUser = userService.create(userCreateRequestDTO);
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);

        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());
        var saved = orderService.create(orderCreateRequestDTO);

        var expected = saved.get();
        assertExpectedIsSaved(expected, expected.getId());

        var orderUpdateRequestDTO = TestDtoBuilder.createOrderUpdateRequestDTO(actualCar.get().getId());
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/" + expected.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("carId", orderUpdateRequestDTO.getCarId().toString())
                                .param("insurance", orderUpdateRequestDTO.getInsurance().toString())
                                .param("startRentalDate", orderUpdateRequestDTO.getStartRentalDate().toString())
                                .param("endRentalDate", orderUpdateRequestDTO.getEndRentalDate().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT + "/" + expected.getId()));
    }

    @Test
    void shouldChangeChangeStatusCorrectly() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var actualUser = userService.create(userCreateRequestDTO);
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);

        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());
        var saved = orderService.create(orderCreateRequestDTO);
        var expected = saved.get();

        assertExpectedIsSaved(expected, expected.getId());

        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/" + expected.getId() + "/change-status");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("status", OrderStatus.DECLINED.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT + "/" + expected.getId()));
    }

    @Test
    void shouldReturn3xxOnDelete() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var actualUser = userService.create(userCreateRequestDTO);
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);

        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());
        var saved = orderService.create(orderCreateRequestDTO);

        assertThat(saved).isPresent();

        mockMvc.perform(post(fromUriString(ENDPOINT + "/" + saved.get().getId() + "/delete").build().encode().toUri())
                        .headers(commonHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> orderService.getById(saved.get().getId()));

        assertEquals("404 NOT_FOUND \"Order with id 3 does not exist.\"", result.getMessage());
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
        var actualUser = userService.create(userCreateRequestDTO);
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);

        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());
        var saved = orderService.create(orderCreateRequestDTO);

        var expected = saved.get();

        var orderUpdateRequestDTO = TestDtoBuilder.createOrderUpdateRequestDTO(actualCar.get().getId());
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/" + expected.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("carId", orderUpdateRequestDTO.getCarId().toString())
                                .param("insurance", orderUpdateRequestDTO.getInsurance().toString())
                                .param("startRentalDate", orderUpdateRequestDTO.getStartRentalDate().toString())
                                .param("endRentalDate", orderUpdateRequestDTO.getEndRentalDate().toString()))
                .andExpect(status().isForbidden());
    }

    private void assertExpectedIsSaved(OrderResponseDto expected, Long id) throws Exception {
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/" + id);
        MvcResult result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(commonHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"))
                .andReturn();

        OrderResponseDto responseDto = (OrderResponseDto) result.getModelAndView().getModel().get("order");

        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getDate()).isEqualTo(expected.getDate());
        assertThat(responseDto.getSum()).isEqualTo(expected.getSum());
        assertThat(responseDto.getInsurance()).isEqualTo(expected.getInsurance());

        assertThat(responseDto.getCar().getId()).isEqualTo(expected.getCar().getId());
        assertThat(responseDto.getCar().getNumber()).isEqualTo(expected.getCar().getNumber());

        assertThat(responseDto.getUser().getUsername()).isEqualTo(expected.getUser().getUsername());
        assertThat(responseDto.getUser().getEmail()).isEqualTo(expected.getUser().getEmail());
    }
}