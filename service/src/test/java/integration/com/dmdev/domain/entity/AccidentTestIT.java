package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Order;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_ACCIDENT_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_ACCIDENT_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_ORDER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccidentTestIT extends IntegrationBaseTest {

    @Test
    void shouldCreateAccident() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var order = session.get(Order.class, TEST_EXISTS_ORDER_ID);
            var accident = TestEntityBuilder.createAccident();
            order.setAccident(accident);

            var savedAccident = session.save(accident);

            assertThat(savedAccident).isNotNull();
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnAccident() {
        try (Session session = sessionFactory.openSession()) {
            var expectedAccident = ExistEntityBuilder.getExistAccident();

            var actualAccident = session.find(Accident.class, TEST_EXISTS_ACCIDENT_ID);

            assertThat(actualAccident).isNotNull();
            assertEquals(expectedAccident, actualAccident);
        }
    }

    @Test
    void shouldUpdateAccident() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var accidentToUpdate = session.find(Accident.class, TEST_EXISTS_ACCIDENT_ID);
            var existOrder = session.find(Order.class, TEST_EXISTS_ORDER_ID);
            accidentToUpdate.setDamage(BigDecimal.valueOf(3456.76));
            accidentToUpdate.setDescription("test description");
            accidentToUpdate.setOrder(existOrder);

            session.update(accidentToUpdate);
            session.flush();
            session.evict(accidentToUpdate);

            var updatedAccident = session.find(Accident.class, accidentToUpdate.getId());

            assertThat(updatedAccident).isEqualTo(accidentToUpdate);
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldDeleteAccident() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var accidentToDelete = session.find(Accident.class, TEST_ACCIDENT_ID_FOR_DELETE);

            session.delete(accidentToDelete);

            assertThat(session.find(Accident.class, accidentToDelete.getId())).isNull();
            session.getTransaction().rollback();
        }
    }
}