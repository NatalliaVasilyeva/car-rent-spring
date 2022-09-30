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
            Order order = session.get(Order.class, TEST_EXISTS_ORDER_ID);
            Accident accident = TestEntityBuilder.createAccident();
            order.setAccident(accident);

            Long savedAccidentId = (Long) session.save(accident);
            session.getTransaction().commit();

            assertThat(savedAccidentId).isNotNull();
        }
    }

    @Test
    void shouldReturnAccident() {
        try (Session session = sessionFactory.openSession()) {
            Accident expectedAccident = ExistEntityBuilder.getExistAccident();

            Accident actualAccident = session.find(Accident.class, TEST_EXISTS_ACCIDENT_ID);

            assertThat(actualAccident).isNotNull();
            assertEquals(expectedAccident, actualAccident);
        }
    }

    @Test
    void shouldUpdateAccident() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Accident accidentToUpdate = session.find(Accident.class, TEST_EXISTS_ACCIDENT_ID);
            Order existOrder = session.find(Order.class, TEST_EXISTS_ORDER_ID);

            accidentToUpdate.setDamage(BigDecimal.valueOf(3456.76));
            accidentToUpdate.setDescription("test description");
            accidentToUpdate.setOrder(existOrder);
            session.update(accidentToUpdate);
            session.flush();
            session.evict(accidentToUpdate);

            Accident updatedAccident = session.find(Accident.class, accidentToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedAccident).isEqualTo(accidentToUpdate);
        }
    }

    @Test
    void shouldDeleteAccident() {
        try (Session session = sessionFactory.openSession()) {
            Accident accidentToDelete = session.find(Accident.class, TEST_ACCIDENT_ID_FOR_DELETE);
            session.beginTransaction();
            session.delete(accidentToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Accident.class, accidentToDelete.getId())).isNull();
        }
    }
}