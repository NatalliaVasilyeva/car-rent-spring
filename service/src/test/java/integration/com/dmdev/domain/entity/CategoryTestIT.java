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

            assertThat(savedCategory).isNotNull();
            session.getTransaction().rollback();
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

            assertThat(categoryToSave.getId()).isNotNull();
            assertThat(modelToSave.getId()).isNotNull();
            assertThat(categoryToSave.getModels()).contains(modelToSave);
            assertThat(modelToSave.getCategory().getId()).isEqualTo(categoryToSave.getId());
            session.getTransaction().rollback();
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

            assertThat(updatedCategory).isEqualTo(categoryToUpdate);
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldDeleteCategory() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var categoryToDelete = session.find(Category.class, TEST_CATEGORY_ID_FOR_DELETE);

            session.delete(categoryToDelete);

            assertThat(session.find(Category.class, categoryToDelete.getId())).isNull();
            session.getTransaction().rollback();
        }
    }
}