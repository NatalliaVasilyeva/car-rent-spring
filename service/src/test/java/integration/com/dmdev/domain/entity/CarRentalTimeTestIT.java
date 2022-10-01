package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.Order;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CAR_RENTAL_TIME_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CAR_RENTAL_TIME_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CarRentalTimeTestIT extends IntegrationBaseTest {

    @Test
    void shouldReturnCarRentalTime() {
        try (Session session = sessionFactory.openSession()) {
            CarRentalTime expectedCarRentalTime = ExistEntityBuilder.getExistCarRentalTime();

            CarRentalTime actualCarRentalTime = session.find(CarRentalTime.class, TEST_EXISTS_CAR_RENTAL_TIME_ID);

            assertThat(actualCarRentalTime).isNotNull();
            assertEquals(expectedCarRentalTime, actualCarRentalTime);
        }
    }

    @Test
    void shouldUpdateCarRentalTime() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarRentalTime carRentalTimeToUpdate = session.find(CarRentalTime.class, TEST_EXISTS_CAR_RENTAL_TIME_ID);

            carRentalTimeToUpdate.setEndRentalDate(LocalDateTime.of(2022, 11, 9, 10, 0));
            session.update(carRentalTimeToUpdate);
            session.flush();
            session.clear();

            CarRentalTime updatedCarRentalTime = session.find(CarRentalTime.class, carRentalTimeToUpdate.getId());
            Order updatedOrder = session.find(Order.class, carRentalTimeToUpdate.getOrder().getId());
            session.getTransaction().commit();

            assertThat(updatedCarRentalTime).isEqualTo(carRentalTimeToUpdate);
            assertThat(updatedOrder.getCarRentalTime()).isEqualTo(updatedCarRentalTime);
        }
    }

    @Test
    void shouldDeleteCarRentalTime() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarRentalTime carRentalTimeToDelete = session.find(CarRentalTime.class, TEST_CAR_RENTAL_TIME_ID_FOR_DELETE);
            carRentalTimeToDelete.getOrder().setCarRentalTime(null);

            session.delete(carRentalTimeToDelete);
            session.getTransaction().commit();

            assertThat(session.find(CarRentalTime.class, carRentalTimeToDelete.getId())).isNull();
        }
    }
}