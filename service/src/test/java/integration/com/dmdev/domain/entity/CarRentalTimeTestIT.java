package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.CarRentalTime;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistTesEntityBuilder;
import integration.com.dmdev.utils.builder.FakeTestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static integration.com.dmdev.utils.TestEntityIdConst.CREATED_TEST_ENTITY_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.DELETED_TEST_ENTITY_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.EXIST_TEST_ENTITY_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarRentalTimeTestIT extends IntegrationBaseTest {

    @Test
    public void shouldCreateCarRentalTime() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedCarRentalTimeId = (Long) session.save(FakeTestEntityBuilder.createCarRentalTime());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedCarRentalTimeId);
        }
    }

    @Test
    public void shouldReturnCarRentalTime() {
        try (Session session = sessionFactory.openSession()) {
            CarRentalTime actualCarRentalTime = session.find(CarRentalTime.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualCarRentalTime).isNotNull();
            assertEquals(ExistTesEntityBuilder.getExistCarRentalTime().getOrderId(), actualCarRentalTime.getOrderId());
            assertEquals(ExistTesEntityBuilder.getExistCarRentalTime().getStartRentalDate(), actualCarRentalTime.getStartRentalDate());
            assertEquals(ExistTesEntityBuilder.getExistCarRentalTime().getEndRentalDate(), actualCarRentalTime.getEndRentalDate());
        }
    }

    @Test
    public void shouldUpdateCarRentalTime() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarRentalTime carRentalTimeToUpdate = ExistTesEntityBuilder.getUpdatedCarRentalTime();
            session.update(carRentalTimeToUpdate);
            session.getTransaction().commit();

            CarRentalTime updatedCarRentalTime = session.find(CarRentalTime.class, carRentalTimeToUpdate.getId());

            assertThat(updatedCarRentalTime).isEqualTo(carRentalTimeToUpdate);
        }
    }

    @Test
    public void shouldDeleteCarRentalTime() {
        try (Session session = sessionFactory.openSession()) {
            Car carToDelete = session.find(Car.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(carToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Car.class, carToDelete.getId())).isNull();
        }
    }
}