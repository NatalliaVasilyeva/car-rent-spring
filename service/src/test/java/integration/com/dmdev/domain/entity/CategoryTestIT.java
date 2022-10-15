package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.Model;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CATEGORY_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CATEGORY_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


class CategoryTestIT extends IntegrationBaseTest {

    @Test
    void shouldCreateCategory() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var categoryToSave = TestEntityBuilder.createCategory();

            var savedCategory = session.save(categoryToSave);
            session.getTransaction().commit();

            assertThat(savedCategory).isNotNull();
        }
    }

    @Test
    void shouldCreateCategoryWithNotExistsModel() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var modelToSave = TestEntityBuilder.createModel();
            var categoryToSave = TestEntityBuilder.createCategory();
            categoryToSave.setModel(modelToSave);

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
            var expectedCategory = ExistEntityBuilder.getExistCategory();

            var actualCategory = session.find(Category.class, TEST_EXISTS_CATEGORY_ID);

            assertThat(actualCategory).isNotNull();
            assertEquals(expectedCategory, actualCategory);
        }
    }

    @Test
    void shouldUpdateCategory() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var categoryToUpdate = session.find(Category.class, TEST_EXISTS_CATEGORY_ID);
            categoryToUpdate.setPrice(BigDecimal.valueOf(90.00));
            categoryToUpdate.setName("test_name");

            session.update(categoryToUpdate);
            session.flush();
            session.evict(categoryToUpdate);

            var updatedCategory = session.find(Category.class, categoryToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedCategory).isEqualTo(categoryToUpdate);
        }
    }

    @Test
    void shouldDeleteCategory() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var categoryToDelete = session.find(Category.class, TEST_CATEGORY_ID_FOR_DELETE);

            session.delete(categoryToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Category.class, categoryToDelete.getId())).isNull();
        }
    }
}