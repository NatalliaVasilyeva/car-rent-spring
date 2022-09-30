package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.entity.Price;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CATEGORY_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CATEGORY_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_PRICE_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


class CategoryTestIT extends IntegrationBaseTest {

    @Test
    void shouldCreateCategory() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Price price = session.get(Price.class, TEST_EXISTS_PRICE_ID);
            Category categoryToSave = TestEntityBuilder.createCategory();
            price.setCategory(categoryToSave);

            Long savedCategoryId = (Long) session.save(categoryToSave);
            session.getTransaction().commit();

            assertThat(savedCategoryId).isNotNull();
        }
    }

    @Test
    void shouldCreateCategoryWithNotExistsModel() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Price price = session.get(Price.class, TEST_EXISTS_PRICE_ID);
            Model modelToSave = TestEntityBuilder.createModel();
            Category categoryToSave = TestEntityBuilder.createCategory();
            categoryToSave.setModel(modelToSave);
            price.setCategory(categoryToSave);

            session.save(categoryToSave);
            session.getTransaction().commit();

            assertThat(categoryToSave.getId()).isNotNull();
            assertThat(modelToSave.getId()).isNotNull();
            assertThat(categoryToSave.getModels()).contains(modelToSave);
            assertThat(modelToSave.getCategory().getId()).isEqualTo(categoryToSave.getId());
        }
    }

    @Test
    void shouldReturnCategory() {
        try (Session session = sessionFactory.openSession()) {
            Category expectedCategory = ExistEntityBuilder.getExistCategory();

            Category actualCategory = session.find(Category.class, TEST_EXISTS_CATEGORY_ID);

            assertThat(actualCategory).isNotNull();
            assertEquals(expectedCategory, actualCategory);
        }
    }

    @Test
    void shouldUpdateCategory() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Category categoryToUpdate = session.find(Category.class, TEST_EXISTS_CATEGORY_ID);
            Price existsPrice = session.find(Price.class, 1L);

            categoryToUpdate.setPrice(existsPrice);
            categoryToUpdate.setName("test_name");
            session.update(categoryToUpdate);
            session.flush();
            session.evict(categoryToUpdate);

            Category updatedCategory = session.find(Category.class, categoryToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedCategory).isEqualTo(categoryToUpdate);
        }
    }

    @Test
    void shouldDeleteCategory() {
        try (Session session = sessionFactory.openSession()) {
            Category categoryToDelete = session.find(Category.class, TEST_CATEGORY_ID_FOR_DELETE);
            session.beginTransaction();
            session.delete(categoryToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Category.class, categoryToDelete.getId())).isNull();
        }
    }
}