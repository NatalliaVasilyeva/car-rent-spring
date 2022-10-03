package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.model.Color;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CAR_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CAR_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CarTestIT extends IntegrationBaseTest {

    @Test
    void shouldCreateCar() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var model = ExistEntityBuilder.getExistModel();
            var car = TestEntityBuilder.createCar();
            model.setCar(car);

            var savedCar = session.save(car);
            session.getTransaction().commit();

            assertThat(savedCar).isNotNull();
        }
    }

    @Test
    void shouldReturnCar() {
        try (Session session = sessionFactory.openSession()) {
            var expectedCar = ExistEntityBuilder.getExistCar();

            var actualCar = session.find(Car.class, TEST_EXISTS_CAR_ID);

            assertThat(actualCar).isNotNull();
            assertEquals(expectedCar, actualCar);
        }
    }

    @Test
    void shouldUpdateCar() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var carToUpdate = session.find(Car.class, TEST_EXISTS_CAR_ID);
            var existModel = session.find(Model.class, 1L);
            carToUpdate.setColor(Color.BLUE);
            carToUpdate.setYear(2010);
            carToUpdate.setModel(existModel);

            session.update(carToUpdate);
            session.flush();
            session.evict(carToUpdate);

            var updatedCar = session.find(Car.class, carToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedCar).isEqualTo(carToUpdate);
        }
    }

    @Test
    void shouldDeleteCar() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var carToDelete = session.find(Car.class, TEST_CAR_ID_FOR_DELETE);

            session.delete(carToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Car.class, carToDelete.getId())).isNull();
        }
    }
}