package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Car;
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

public class CarTestIT extends IntegrationBaseTest {

    @Test
    public void shouldCreateCar() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedCarId = (Long) session.save(FakeTestEntityBuilder.createCar());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedCarId);
        }
    }

    @Test
    public void shouldReturnCar() {
        try (Session session = sessionFactory.openSession()) {
            Car actualCar = session.find(Car.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualCar).isNotNull();
            assertEquals(ExistTesEntityBuilder.getExistCar().getCarNumber(), actualCar.getCarNumber());
            assertEquals(ExistTesEntityBuilder.getExistCar().getIsRepaired(), actualCar.getIsRepaired());
            assertEquals(ExistTesEntityBuilder.getExistCar().getColor(), actualCar.getColor());
            assertEquals(ExistTesEntityBuilder.getExistCar().getYear(), actualCar.getYear());
            assertEquals(ExistTesEntityBuilder.getExistCar().getVin(), actualCar.getVin());
        }
    }

    @Test
    public void shouldUpdateCar() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Car carToUpdate = ExistTesEntityBuilder.getUpdatedCar();
            session.update(carToUpdate);
            session.getTransaction().commit();

            Car updatedCar = session.find(Car.class, carToUpdate.getId());

            assertThat(updatedCar).isEqualTo(carToUpdate);
        }
    }

    @Test
    public void shouldDeleteCar() {
        try (Session session = sessionFactory.openSession()) {
            Car carToDelete = session.find(Car.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(carToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Car.class, carToDelete.getId())).isNull();
        }
    }
}