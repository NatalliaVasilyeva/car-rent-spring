package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.Order;
import com.dmdev.domain.entity.User;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CAR_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_ORDER_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_USER_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_ORDER_ID_FOR_DELETE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTestIT extends IntegrationBaseTest {

    @Test
    void shouldCreateOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var user = session.get(User.class, TEST_EXISTS_USER_ID);
            var car = session.get(Car.class, TEST_EXISTS_CAR_ID);
            var orderToSave = TestEntityBuilder.createOrder();
            orderToSave.setUser(user);
            orderToSave.setCar(car);
            var carRentalTime = TestEntityBuilder.createCarRentalTime();
            carRentalTime.setOrder(orderToSave);

            var savedOrder = session.save(orderToSave);
            session.getTransaction().commit();

            assertThat(savedOrder).isNotNull();
        }
    }

    @Test
    void shouldCreateOrderWithNotExistsAccidents() {
        try (Session session = sessionFactory.openSession()) {
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

            session.save(orderToSave);
            session.getTransaction().commit();

            assertThat(orderToSave.getId()).isNotNull();
            assertThat(accidentToSave.getId()).isNotNull();
            assertThat(orderToSave.getAccidents()).contains(accidentToSave);
            assertThat(accidentToSave.getOrder().getId()).isEqualTo(orderToSave.getId());
        }
    }


    @Test
    void shouldReturnOrder() {
        try (Session session = sessionFactory.openSession()) {
            var expectedOrder = ExistEntityBuilder.getExistOrder();

            var actualOrder = session.find(Order.class, TEST_EXISTS_ORDER_ID);

            assertThat(actualOrder).isNotNull();
            assertEquals(expectedOrder, actualOrder);
        }
    }

    @Test
    void shouldUpdateOrder() {
        try (Session session = sessionFactory.openSession()) {
            var startRentalDate = LocalDateTime.of(2022, 10, 11, 13, 0);
            session.beginTransaction();
            var orderToUpdate = session.find(Order.class, TEST_EXISTS_ORDER_ID);
            var carRentalTime = orderToUpdate.getCarRentalTime();
            carRentalTime.setStartRentalDate(startRentalDate);
            orderToUpdate.setInsurance(false);
            carRentalTime.setOrder(orderToUpdate);

            session.update(orderToUpdate);
            session.flush();
            session.clear();

            var updatedOrder = session.find(Order.class, orderToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedOrder).isEqualTo(orderToUpdate);
            assertThat(updatedOrder.getCarRentalTime().getStartRentalDate()).isEqualTo(startRentalDate);
        }
    }

    @Test
    void shouldDeleteOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var orderToDelete = session.find(Order.class, TEST_ORDER_ID_FOR_DELETE);

            session.delete(orderToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Order.class, orderToDelete.getId())).isNull();
            assertThat(session.find(CarRentalTime.class, orderToDelete.getCarRentalTime().getId())).isNull();
        }
    }
}