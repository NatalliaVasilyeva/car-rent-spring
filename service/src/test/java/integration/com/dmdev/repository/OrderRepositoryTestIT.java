package integration.com.dmdev.repository;

import com.dmdev.domain.dto.filterdto.OrderFilter;
import com.dmdev.domain.entity.Order;
import com.dmdev.domain.model.OrderStatus;
import com.dmdev.domain.projection.OrderFullView;
import com.dmdev.repository.CarRepository;
import com.dmdev.repository.OrderRepository;
import com.dmdev.repository.UserRepository;
import com.dmdev.utils.predicate.OrderPredicateBuilder;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CAR_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_ORDER_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_USER_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_ORDER_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private OrderPredicateBuilder orderPredicateBuilder;

    @Test
    void shouldSaveOrder() {
        var user = userRepository.findById(TEST_EXISTS_USER_ID).get();
        var car = carRepository.findById(TEST_EXISTS_CAR_ID).get();
        var orderToSave = TestEntityBuilder.createOrder();
        orderToSave.setUser(user);
        orderToSave.setCar(car);
        var carRentalTime = TestEntityBuilder.createCarRentalTime();
        carRentalTime.setOrder(orderToSave);

        var savedOrder = orderRepository.save(orderToSave);

        assertThat(savedOrder).isNotNull();
    }

    @Test
    void shouldCreateOrderWithNotExistsAccidents() {
        var user = userRepository.findById(TEST_EXISTS_USER_ID).get();
        var car = carRepository.findById(TEST_EXISTS_CAR_ID).get();
        var accidentToSave = TestEntityBuilder.createAccident();
        var orderToSave = TestEntityBuilder.createOrder();
        orderToSave.setUser(user);
        orderToSave.setCar(car);
        orderToSave.setAccident(accidentToSave);
        var carRentalTime = TestEntityBuilder.createCarRentalTime();
        carRentalTime.setOrder(orderToSave);

        orderRepository.save(orderToSave);

        assertThat(orderToSave.getId()).isNotNull();
        assertThat(accidentToSave.getId()).isNotNull();
        assertThat(orderToSave.getAccidents()).contains(accidentToSave);
        assertThat(accidentToSave.getOrder().getId()).isEqualTo(orderToSave.getId());
    }


    @Test
    void shouldFindByIdOrder() {
        var expectedOrder = Optional.of(ExistEntityBuilder.getExistOrder());

        var actualOrder = orderRepository.findById(TEST_EXISTS_ORDER_ID);

        assertThat(actualOrder).isNotNull();
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void shouldUpdateOrder() {
        var startRentalDate = LocalDateTime.of(2022, 10, 11, 13, 0);
        var orderToUpdate = orderRepository.findById(TEST_EXISTS_ORDER_ID).get();
        var carRentalTime = orderToUpdate.getCarRentalTime();
        carRentalTime.setStartRentalDate(startRentalDate);
        orderToUpdate.setInsurance(false);
        carRentalTime.setOrder(orderToUpdate);

        orderRepository.save(orderToUpdate);

        var updatedOrder = orderRepository.findById(orderToUpdate.getId()).get();

        assertThat(updatedOrder).isEqualTo(orderToUpdate);
        assertThat(updatedOrder.getCarRentalTime().getStartRentalDate()).isEqualTo(startRentalDate);
    }

    @Test
    void shouldDeleteOrder() {
        var order = orderRepository.findById(TEST_ORDER_ID_FOR_DELETE);

        order.ifPresent(or -> orderRepository.delete(or));

        assertThat(orderRepository.findById(TEST_ORDER_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllOrders() {
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(2);

        List<String> passports = orders.stream().map(Order::getPassport).collect(toList());
        assertThat(passports).containsExactlyInAnyOrder("MP1234567", "MP1234589");
    }


    @Test
    void shouldReturnOrdersByCarNumber() {
        List<Order> orders = orderRepository.findAllByCarNumber("7834AE-7");

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0)).isEqualTo(ExistEntityBuilder.getExistOrder());
    }

    @Test
    void shouldReturnOrdersByCarId() {
        List<Order> orders = orderRepository.findAllByCarId(TEST_EXISTS_CAR_ID);

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0)).isEqualTo(ExistEntityBuilder.getExistOrder());
    }

    @Test
    void shouldReturnOrdersByUserId() {
        List<Order> orders = orderRepository.findAllByUserId(TEST_EXISTS_USER_ID);

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0)).isEqualTo(ExistEntityBuilder.getExistOrder());
    }

    @Test
    void shouldReturnOrdersByDates() {
        List<Order> orders = orderRepository.findAllByDateBetween(LocalDate.of(2021, 5, 10), LocalDate.of(2021, 12, 10));

        assertThat(orders).isEmpty();
    }

    @Test
    void shouldReturnOrdersByDate() {
        List<Order> orders = orderRepository.findAllByDate(LocalDate.of(2022, 7, 2));

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0)).isEqualTo(ExistEntityBuilder.getExistOrder());
    }

    @Test
    void shouldReturnOrdersWithAccidents() {
        List<Order> orders = orderRepository.findAllWithAccidents();

        assertThat(orders).hasSize(2).contains(ExistEntityBuilder.getExistOrder());
    }

    @Test
    void shouldReturnOrdersOrderStatus() {
        List<Order> orders = orderRepository.findAllByOrderStatus(OrderStatus.PAYED);

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0)).isEqualTo(ExistEntityBuilder.getExistOrder());
    }

    @Test
    void shouldReturnOrdersByFilterWithStatusAndCarNumber() {
        var orderFilter = OrderFilter.builder()
                .orderStatus(OrderStatus.PAYED)
                .carNumber("7834AE-7")
                .build();

        List<Order> orders = IterableUtils.toList(orderRepository.findAll(orderPredicateBuilder.build(orderFilter)));

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0)).isEqualTo(ExistEntityBuilder.getExistOrder());
    }

    @Test
    void shouldFindAllOrdersFullView() {
        List<OrderFullView> orders = orderRepository.findAllFullView();
        assertThat(orders).hasSize(2);

        List<OrderStatus> orderStatuses = orders.stream().map(OrderFullView::getOrderStatus).collect(toList());
        assertThat(orderStatuses).containsExactlyInAnyOrder(OrderStatus.PAYED, OrderStatus.CONFIRMATION_WAIT);
    }
}