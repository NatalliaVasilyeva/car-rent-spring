package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Price;
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

public class PriceTestIT extends IntegrationBaseTest {

    @Test
    public void shouldCreatePrice() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedPriceId = (Long) session.save(FakeTestEntityBuilder.createPrice());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedPriceId);
        }
    }

    @Test
    public void shouldReturnPrice() {
        try (Session session = sessionFactory.openSession()) {
            Price actualPrice = session.find(Price.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualPrice).isNotNull();
            assertEquals(ExistTesEntityBuilder.getExistPrice().getPrice(), actualPrice.getPrice());
        }
    }

    @Test
    public void shouldUpdatePrice() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Price priceToUpdate = ExistTesEntityBuilder.getUpdatedPrice();
            session.update(priceToUpdate);
            session.getTransaction().commit();

            Price updatedPrice = session.find(Price.class, priceToUpdate.getId());

            assertThat(updatedPrice).isEqualTo(priceToUpdate);
        }
    }

    @Test
    public void shouldDeletePrice() {
        try (Session session = sessionFactory.openSession()) {
            Price priceToDelete = session.find(Price.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(priceToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Price.class, priceToDelete.getId())).isNull();
        }
    }
}