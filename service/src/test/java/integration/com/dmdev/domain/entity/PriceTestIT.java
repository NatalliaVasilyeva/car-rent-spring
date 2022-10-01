package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.Price;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_PRICE_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_PRICE_ID_FOR_DELETE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceTestIT extends IntegrationBaseTest {

    @Test
    void shouldCreatePrice() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedPriceId = (Long) session.save(TestEntityBuilder.createPrice());
            session.getTransaction().commit();

            assertThat(savedPriceId).isNotNull();
        }
    }

    @Test
    void shouldCreateCategoryWithNotExistscategory() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Category categoryToSave = TestEntityBuilder.createCategory();
            Price priceToSave = TestEntityBuilder.createPrice();
            priceToSave.setCategory(categoryToSave);

            session.save(priceToSave);
            session.getTransaction().commit();

            assertThat(priceToSave.getId()).isNotNull();
            assertThat(categoryToSave.getId()).isNotNull();
            assertThat(priceToSave.getCategories()).contains(categoryToSave);
            assertThat(categoryToSave.getPrice().getId()).isEqualTo(priceToSave.getId());
        }
    }

    @Test
    void shouldReturnPrice() {
        try (Session session = sessionFactory.openSession()) {
            Price expectedPrice = ExistEntityBuilder.getExistPrice();

            Price actualPrice = session.find(Price.class, TEST_EXISTS_PRICE_ID);

            assertThat(actualPrice).isNotNull();
            assertEquals(expectedPrice, actualPrice);
        }
    }

    @Test
    void shouldUpdatePrice() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Price priceToUpdate = session.find(Price.class, TEST_EXISTS_PRICE_ID);
            priceToUpdate.setSum(BigDecimal.valueOf(67.90));

            session.update(priceToUpdate);
            session.flush();
            session.evict(priceToUpdate);

            Price updatedPrice = session.find(Price.class, priceToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedPrice).isEqualTo(priceToUpdate);
            assertThat(updatedPrice).isEqualTo(priceToUpdate);
        }
    }

    @Test
    void shouldDeletePrice() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Price priceToDelete = session.find(Price.class, TEST_PRICE_ID_FOR_DELETE);

            session.delete(priceToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Price.class, priceToDelete.getId())).isNull();
        }
    }
}