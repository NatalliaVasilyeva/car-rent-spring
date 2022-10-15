package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Model;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_BRAND_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_BRAND_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BrandTestIT extends IntegrationBaseTest {

    @Test
    void shouldCreateBrandWithoutModel() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var savedBrand = session.save(TestEntityBuilder.createBrand());

            assertThat(savedBrand).isNotNull();
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldCreateBrandWithNotExistsModel() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var modelToSave = TestEntityBuilder.createModel();
            var brandToSave = TestEntityBuilder.createBrand();
            brandToSave.setModel(modelToSave);

            session.save(brandToSave);

            assertThat(brandToSave.getId()).isNotNull();
            assertThat(modelToSave.getId()).isNotNull();
            assertThat(brandToSave.getModels()).contains(modelToSave);
            assertThat(modelToSave.getBrand().getId()).isEqualTo(brandToSave.getId());
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnBrand() {
        try (Session session = sessionFactory.openSession()) {
            var expectedBrand = ExistEntityBuilder.getExistBrand();

            var actualBrand = session.find(Brand.class, TEST_EXISTS_BRAND_ID);

            assertThat(actualBrand).isNotNull();
            assertEquals(expectedBrand, actualBrand);
        }
    }

    @Test
    void shouldUpdateBrand() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var brandToUpdate = session.find(Brand.class, TEST_EXISTS_BRAND_ID);
            brandToUpdate.setName("pegas");

            session.update(brandToUpdate);
            session.flush();
            session.evict(brandToUpdate);

            var updatedBrand = session.find(Brand.class, brandToUpdate.getId());

            assertThat(updatedBrand).isEqualTo(brandToUpdate);
            assertEquals(brandToUpdate.getName(), updatedBrand.getModels().stream().map(Model::getBrand).findFirst().get().getName());
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldDeleteBrand() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var brandToDelete = session.find(Brand.class, TEST_BRAND_ID_FOR_DELETE);

            session.delete(brandToDelete);

            assertThat(session.find(Brand.class, brandToDelete.getId())).isNull();
            session.getTransaction().rollback();
        }
    }
}