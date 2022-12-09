package integration.com.dmdev.service;

import com.dmdev.domain.dto.filterdto.OrderFilter;
import com.dmdev.domain.dto.order.OrderResponseDto;
import com.dmdev.domain.dto.order.OrderUpdateRequestDto;
import com.dmdev.domain.model.OrderStatus;
import com.dmdev.service.BrandService;
import com.dmdev.service.CarService;
import com.dmdev.service.ModelService;
import com.dmdev.service.OrderService;
import com.dmdev.service.UserService;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.TestDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_ORDER_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class OrderServiceTestIT extends IntegrationBaseTest {

    private final OrderService orderService;
    private final UserService userService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final CarService carService;

    @Test
    void shouldSaveOrderCorrectly() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var actualUser = userService.create(userCreateRequestDTO);

        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);

        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());

        var actualOrder = orderService.create(orderCreateRequestDTO);

        assertTrue(actualOrder.isPresent());
        assertEquals(orderCreateRequestDTO.getUserId(), actualOrder.get().getUser().getId());
        assertEquals(orderCreateRequestDTO.getCarId(), actualOrder.get().getCar().getId());
        assertEquals(orderCreateRequestDTO.getInsurance(), actualOrder.get().getInsurance());
        assertEquals(orderCreateRequestDTO.getStartRentalDate(), actualOrder.get().getStartRentalDate());
        assertEquals(orderCreateRequestDTO.getEndRentalDate(), actualOrder.get().getEndRentalDate());
    }

    @Test
    void shouldFindAllOrders() {
        Page<OrderResponseDto> orders = orderService.getAll(OrderFilter.builder().build(), 0, 4);

        assertThat(orders.getContent()).hasSize(2);
        assertThat(orders.getTotalElements()).isEqualTo(2L);
        assertThat(orders.getNumberOfElements()).isEqualTo(2L);

        List<LocalDateTime> startTimes = orders.getContent().stream().map(OrderResponseDto::getStartRentalDate).collect(toList());
        assertThat(startTimes).containsExactlyInAnyOrder(LocalDateTime.of(2022, 7, 2, 0, 0, 0), LocalDateTime.of(2022, 9, 2, 0, 0, 0));
    }

    @Test
    void shouldReturnOrdersByFilter() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var actualUser = userService.create(userCreateRequestDTO);

        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);

        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());

        var orderResponseDto = orderService.create(orderCreateRequestDTO);

        var orderFilter = OrderFilter.builder()
                .orderStatus(OrderStatus.CONFIRMATION_WAIT)
                .build();

        Page<OrderResponseDto> orders = orderService.getAll(orderFilter, 0, 4);

        assertThat(orders.getContent()).hasSize(2);
        assertThat(orders.getTotalElements()).isEqualTo(2L);
        assertThat(orders.getNumberOfElements()).isEqualTo(2L);

        assertThat(orders.getContent().get(1).getSum()).isEqualTo(orderResponseDto.get().getSum());
        assertThat(orders.getContent().get(1).getOrderStatus()).isEqualTo(orderResponseDto.get().getOrderStatus());
        assertThat(orders.getContent().get(1).getCar().getNumber()).isEqualTo(orderResponseDto.get().getCar().getNumber());
    }

    @Test
    void shouldReturnOrderById() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var actualUser = userService.create(userCreateRequestDTO);
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);
        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());
        var expectedOrder = orderService.create(orderCreateRequestDTO);

        var actualOrder = orderService.getById(expectedOrder.get().getId());

        assertThat(actualOrder).isNotNull();
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void shouldReturnOrdersByStatusCorrectly() {
        var orders = orderService.getAllByStatus(OrderStatus.DECLINED);

        assertThat(orders).isNotNull().hasSize(0);
    }

    @Test
    void shouldReturnOrdersByUserIdCorrectly() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var actualUser = userService.create(userCreateRequestDTO);
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);
        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());
        var savedOrder = orderService.create(orderCreateRequestDTO);

        var orders = orderService.getAllByUserId(savedOrder.get().getId());

        assertThat(orders).isNotNull().hasSize(1);
        assertEquals(orders.get(0).getInsurance(), savedOrder.get().getInsurance());
    }

    @Test
    void shouldReturnOrdersByUserIdStatusAndSumCorrectly() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var actualUser = userService.create(userCreateRequestDTO);
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);
        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());
        var savedOrder = orderService.create(orderCreateRequestDTO);

        var orders = orderService.getAllByUserId(savedOrder.get().getId(), OrderStatus.DECLINED, new BigDecimal(1000));

        assertThat(orders).isEmpty();
    }

    @Test
    void shouldUpdateOrderCorrectly() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var actualUser = userService.create(userCreateRequestDTO);
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);
        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());
        var savedOrder = orderService.create(orderCreateRequestDTO);

        var orderUpdateRequestDto = new OrderUpdateRequestDto(
                actualCar.get().getId(),
                false,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10));

        var actualOrder = orderService.update(savedOrder.get().getId(), orderUpdateRequestDto);

        assertThat(actualOrder).isNotNull();
        actualOrder.ifPresent(order -> {
            assertEquals(orderUpdateRequestDto.getInsurance(), order.getInsurance());
            assertEquals(orderUpdateRequestDto.getStartRentalDate(), order.getStartRentalDate());
            assertSame(orderUpdateRequestDto.getEndRentalDate(), order.getEndRentalDate());
        });
    }

    @Test
    void shouldChangeOrderStatusCorrectly() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();
        var actualUser = userService.create(userCreateRequestDTO);
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var actualCar = carService.create(carCreateRequestDto);
        var orderCreateRequestDTO = TestDtoBuilder.createOrderRequestDto(actualUser.get().getId(), actualCar.get().getId());
        var savedOrder = orderService.create(orderCreateRequestDTO);

        var actualOrder = orderService.changeOrderStatus(savedOrder.get().getId(), OrderStatus.CANCELLED);

        assertThat(actualOrder).isNotNull();
        actualOrder.ifPresent(order -> {
            assertEquals(order.getOrderStatus(), OrderStatus.CANCELLED);
        });
    }

    @Test
    void shouldDeleteOrderByIdCorrectly() {
        assertTrue(orderService.deleteById(TEST_EXISTS_ORDER_ID));
    }

    @Test
    void shouldNotDeleteOrderWithNonExistsId() {
        assertFalse(orderService.deleteById(999999L));
    }
}