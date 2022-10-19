package integration.com.dmdev.repository;

import com.dmdev.domain.dto.OrderFilter;
import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.Order;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.model.OrderStatus;
import com.dmdev.repository.OrderRepository;
import com.querydsl.core.Tuple;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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

    private final Session session = createProxySession(sessionFactory);
    private final OrderRepository orderRepository = new OrderRepository(session);

    @Test
    void shouldSaveOrder() {
        session.beginTransaction();
        var user = session.get(User.class, TEST_EXISTS_USER_ID);
        var car = session.get(Car.class, TEST_EXISTS_CAR_ID);
        var orderToSave = TestEntityBuilder.createOrder();
        orderToSave.setUser(user);
        orderToSave.setCar(car);
        var carRentalTime = TestEntityBuilder.createCarRentalTime();
        carRentalTime.setOrder(orderToSave);

        var savedOrder = orderRepository.save(orderToSave);

        assertThat(savedOrder).isNotNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldCreateOrderWithNotExistsAccidents() {
        session.beginTransaction();
        var user = session.get(User.class, TEST_EXISTS_USER_ID);
        var car = session.get(Car.class, TEST_EXISTS_CAR_ID);
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
        session.getTransaction().rollback();
    }


    @Test
    void shouldFindByIdOrder() {
        session.beginTransaction();
        var expectedOrder = Optional.of(ExistEntityBuilder.getExistOrder());

        var actualOrder = orderRepository.findById(TEST_EXISTS_ORDER_ID);

        assertThat(actualOrder).isNotNull();
        assertEquals(expectedOrder, actualOrder);
        session.getTransaction().rollback();
    }

    @Test
    void shouldUpdateOrder() {
        session.beginTransaction();
        var startRentalDate = LocalDateTime.of(2022, 10, 11, 13, 0);
        var orderToUpdate = session.find(Order.class, TEST_EXISTS_ORDER_ID);
        var carRentalTime = orderToUpdate.getCarRentalTime();
        carRentalTime.setStartRentalDate(startRentalDate);
        orderToUpdate.setInsurance(false);
        carRentalTime.setOrder(orderToUpdate);

        orderRepository.update(orderToUpdate);
        session.clear();

        var updatedOrder = session.find(Order.class, orderToUpdate.getId());

        assertThat(updatedOrder).isEqualTo(orderToUpdate);
        assertThat(updatedOrder.getCarRentalTime().getStartRentalDate()).isEqualTo(startRentalDate);
        session.getTransaction().rollback();
    }

    @Test
    void shouldDeleteOrder() {
        session.beginTransaction();

        Order order = session.find(Order.class, TEST_ORDER_ID_FOR_DELETE);
        orderRepository.delete(order);

        assertThat(session.find(Order.class, TEST_ORDER_ID_FOR_DELETE)).isNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindAllOrders() {
        session.beginTransaction();

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(2);

        List<String> passports = orders.stream().map(Order::getPassport).collect(toList());
        assertThat(passports).containsExactlyInAnyOrder("MP1234567", "MP1234589");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllOrdersWithQueryDsl() {
        session.beginTransaction();

        List<Order> orders = orderRepository.findAllQueryDsl();
        assertThat(orders).hasSize(2);

        List<LocalDate> ordersData = orders.stream().map(Order::getDate).collect(toList());
        assertThat(ordersData).contains(LocalDate.of(2022, 7, 1), LocalDate.of(2022, 7, 2));

        List<String> carsNumber = orders.stream()
                .map(Order::getCar)
                .map(Car::getCarNumber)
                .collect(toList());
        assertThat(carsNumber).contains("7865AE-7", "7834AE-7");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnOrderByIdWithQueryDsl() {
        session.beginTransaction();

        Optional<Order> optionalOrder = orderRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_ORDER_ID);

        assertThat(optionalOrder).isNotNull();
        optionalOrder.ifPresent(order -> assertThat(order.getId()).isEqualTo(ExistEntityBuilder.getExistOrder().getId()));
        assertThat(optionalOrder).isEqualTo(Optional.of(ExistEntityBuilder.getExistOrder()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnOrdersByCarNumberCriteria() {
        session.beginTransaction();

        List<Order> orders = orderRepository.findOrdersByCarNumberCriteria("7834AE-7");

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0)).isEqualTo(ExistEntityBuilder.getExistOrder());
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnOrdersOrderStatusCriteria() {
        session.beginTransaction();

        List<Order> orders = orderRepository.findOrdersByOrderStatusCriteria(OrderStatus.PAYED);

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0)).isEqualTo(ExistEntityBuilder.getExistOrder());
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnOrdersByBrandNameAndModelNameOrderByDateQueryDsl() {
        session.beginTransaction();
        OrderFilter orderFilter = OrderFilter.builder()
                .brandName("mercedes")
                .modelName("Benz")
                .build();

        List<Order> orders = orderRepository.findOrdersByBrandNameAndModelNameOrderByDateQueryDsl(orderFilter);

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0)).isEqualTo(ExistEntityBuilder.getExistOrder());
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnOrdersWhereAccidentsSumMoreThanAvgOrderByDateQueryDsl() {
        session.beginTransaction();

        List<Order> orders = orderRepository.findOrdersWhereAccidentsSumMoreThanAvgSumOrderByDateQueryDsl();

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0)
                .getAccidents()
                .stream()
                .map(Accident::getDamage)
                .collect(toList()))
                .contains(BigDecimal.valueOf(75.50).setScale(2));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnTuplesWithAvgSumAndDateOrderByDateQueryDsl() {
        session.beginTransaction();

        List<Tuple> orders = orderRepository.findOrderTuplesWithAvgSumAndDateOrderByDateQueryDsl();

        assertThat(orders).hasSize(2);
        List<LocalDate> dates = orders.stream().map(r -> r.get(0, LocalDate.class)).collect(toList());
        assertThat(dates).containsAll(List.of(LocalDate.of(2022, 7, 2), LocalDate.of(2022, 7, 1)));
        session.getTransaction().rollback();
    }
}