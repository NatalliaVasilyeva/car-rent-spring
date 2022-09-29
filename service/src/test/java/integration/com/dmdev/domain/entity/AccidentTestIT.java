package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Accident;
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

public class AccidentTestIT extends IntegrationBaseTest {

    @Test
    public void shouldCreateAccident() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedAccidentId = (Long) session.save(FakeTestEntityBuilder.createAccident());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedAccidentId);
        }
    }

    @Test
    public void shouldReturnAccident() {
        try (Session session = sessionFactory.openSession()) {
            Accident actualAccident = session.find(Accident.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualAccident).isNotNull();
            assertEquals(ExistTesEntityBuilder.getExistAccident().getDamage(), actualAccident.getDamage());
            assertEquals(ExistTesEntityBuilder.getExistAccident().getDescription(), actualAccident.getDescription());
            assertEquals(ExistTesEntityBuilder.getExistAccident().getOrderId(), actualAccident.getOrderId());
            assertEquals(ExistTesEntityBuilder.getExistAccident().getAccidentDate(), actualAccident.getAccidentDate());
        }
    }

    @Test
    public void shouldUpdateAccident() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Accident accidentToUpdate = ExistTesEntityBuilder.getUpdatedAccident();
            session.update(accidentToUpdate);
            session.getTransaction().commit();

            Accident updatedAccident = session.find(Accident.class, accidentToUpdate.getId());

            assertThat(updatedAccident).isEqualTo(accidentToUpdate);
        }
    }

    @Test
    public void shouldDeleteAccident() {
        try (Session session = sessionFactory.openSession()) {
            Accident accidentToDelete = session.find(Accident.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(accidentToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Accident.class, accidentToDelete.getId())).isNull();
        }
    }
}