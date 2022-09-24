package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Category;
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

public class CategoryTestIT extends IntegrationBaseTest {

    @Test
    public void shouldCreateCategory() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedCategoryId = (Long) session.save(FakeTestEntityBuilder.createCategory());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedCategoryId);
        }
    }

    @Test
    public void shouldReturnCategory() {
        try (Session session = sessionFactory.openSession()) {
            Category actualCategory = session.find(Category.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualCategory).isNotNull();
            assertEquals(ExistTesEntityBuilder.getExistCategory().getName(), actualCategory.getName());
            assertEquals(ExistTesEntityBuilder.getExistCategory().getPriceId(), actualCategory.getPriceId());
        }
    }

    @Test
    public void shouldUpdateCategory() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Category categoryToUpdate = ExistTesEntityBuilder.getUpdatedCategory();
            session.update(categoryToUpdate);
            session.getTransaction().commit();

            Category updatedCategory = session.find(Category.class, categoryToUpdate.getId());

            assertThat(updatedCategory).isEqualTo(categoryToUpdate);
        }
    }

    @Test
    public void shouldDeleteCategory() {
        try (Session session = sessionFactory.openSession()) {
            Category categoryToDelete = session.find(Category.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(categoryToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Category.class, categoryToDelete.getId())).isNull();
        }
    }
}