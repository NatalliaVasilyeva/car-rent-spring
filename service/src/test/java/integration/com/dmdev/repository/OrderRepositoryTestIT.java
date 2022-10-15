package integration.com.dmdev.repository;

import com.dmdev.domain.dto.OrderFilter;
import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.Order;
import com.dmdev.domain.model.OrderStatus;
import com.dmdev.repository.OrderRepository;
import com.querydsl.core.Tuple;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryTestIT extends IntegrationBaseTest {

    private final OrderRepository orderRepository = OrderRepository.getInstance();

    @Test
    void shouldReturnAllOrdersWithHql() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Order> orders = orderRepository.findAllHql(session);

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
    }

    @Test
    void shouldReturnAllOrdersWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Order> orders = orderRepository.findAllCriteria(session);

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
    }

    @Test
    void shouldReturnAllOrdersWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Order> orders = orderRepository.findAllQueryDsl(session);

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
    }

    @Test
    void shouldReturnOrderByIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Order> optionalOrder = orderRepository.findByIdCriteria(session, TestEntityIdConst.TEST_EXISTS_ORDER_ID);

            assertThat(optionalOrder).isNotNull();
            optionalOrder.ifPresent(order -> assertThat(order.getId()).isEqualTo(ExistEntityBuilder.getExistOrder().getId()));
            assertThat(optionalOrder).isEqualTo(Optional.of(ExistEntityBuilder.getExistOrder()));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnOrderByIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Order> optionalOrder = orderRepository.findByIdQueryDsl(session, TestEntityIdConst.TEST_EXISTS_ORDER_ID);

            assertThat(optionalOrder).isNotNull();
            optionalOrder.ifPresent(order -> assertThat(order.getId()).isEqualTo(ExistEntityBuilder.getExistOrder().getId()));
            assertThat(optionalOrder).isEqualTo(Optional.of(ExistEntityBuilder.getExistOrder()));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnOrdersByCarNumberCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Order> orders = orderRepository.findOrdersByCarNumberCriteria(session, "7834AE-7");

            assertThat(orders).hasSize(1);
            assertThat(orders.get(0)).isEqualTo(ExistEntityBuilder.getExistOrder());
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnOrdersOrderStatusCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Order> orders = orderRepository.findOrdersByOrderStatusCriteria(session, OrderStatus.PAYED);

            assertThat(orders).hasSize(1);
            assertThat(orders.get(0)).isEqualTo(ExistEntityBuilder.getExistOrder());
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnOrdersByBrandNameAndModelNameOrderByDateQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            OrderFilter orderFilter = OrderFilter.builder()
                    .brandName("mercedes")
                    .modelName("Benz")
                    .build();

            List<Order> orders = orderRepository.findOrdersByBrandNameAndModelNameOrderByDateQueryDsl(session, orderFilter);

            assertThat(orders).hasSize(1);
            assertThat(orders.get(0)).isEqualTo(ExistEntityBuilder.getExistOrder());
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnOrdersWhereAccidentsSumMoreThanAvgOrderByDateQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Order> orders = orderRepository.findOrdersWhereAccidentsSumMoreThanAvgSumOrderByDateQueryDsl(session);

            assertThat(orders).hasSize(1);
            assertThat(orders.get(0)
                    .getAccidents()
                    .stream()
                    .map(Accident::getDamage)
                    .collect(toList()))
                    .contains(BigDecimal.valueOf(75.50).setScale(2));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnTuplesWithAvgSumAndDateOrderByDateQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Tuple> orders = orderRepository.findOrderTuplesWithAvgSumAndDateOrderByDateQueryDsl(session);

            assertThat(orders).hasSize(2);
            List<LocalDate> dates = orders.stream().map(r -> r.get(0, LocalDate.class)).collect(toList());
            assertThat(dates).containsAll(List.of(LocalDate.of(2022, 7, 2), LocalDate.of(2022, 7, 1)));
            session.getTransaction().rollback();
        }
    }
}