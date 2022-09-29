package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Brand;
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

public class BrandTestIT extends IntegrationBaseTest {

    @Test
    public void shouldCreateBrand() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedBrandId = (Long) session.save(FakeTestEntityBuilder.createBrand());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedBrandId);
        }
    }

    @Test
    public void shouldReturnBrand() {
        try (Session session = sessionFactory.openSession()) {
            Brand actualBrand = session.find(Brand.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualBrand).isNotNull();
            assertEquals(ExistTesEntityBuilder.getExistBrand().getName(), actualBrand.getName());
        }
    }

    @Test
    public void shouldUpdateBrand() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Brand brandToUpdate = ExistTesEntityBuilder.getUpdatedBrand();
            session.update(brandToUpdate);
            session.getTransaction().commit();

            Brand updatedBrand = session.find(Brand.class, brandToUpdate.getId());

            assertThat(updatedBrand).isEqualTo(brandToUpdate);
        }
    }

    @Test
    public void shouldDeleteBrand() {
        try (Session session = sessionFactory.openSession()) {
            Brand brandToDelete = session.find(Brand.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(brandToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Brand.class, brandToDelete.getId())).isNull();
        }
    }
}