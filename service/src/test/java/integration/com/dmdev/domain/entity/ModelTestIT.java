package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.model.EngineType;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_BRAND_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CATEGORY_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_MODEL_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_MODEL_ID_FOR_DELETE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelTestIT extends IntegrationBaseTest {

    @Test
    void shouldCreateModel() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Brand brand = session.get(Brand.class, TEST_EXISTS_BRAND_ID);
            Category category = session.get(Category.class, TEST_EXISTS_CATEGORY_ID);
            Model modelToSave = TestEntityBuilder.createModel();
            brand.setModel(modelToSave);
            category.setModel(modelToSave);

            Long savedModelId = (Long) session.save(modelToSave);
            session.getTransaction().commit();

            assertThat(savedModelId).isNotNull();
        }
    }

    @Test
    void shouldCreateModelWithNotExistsCar() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Brand brand = session.get(Brand.class, TEST_EXISTS_BRAND_ID);
            Category category = session.get(Category.class, TEST_EXISTS_CATEGORY_ID);
            Car carToSave = TestEntityBuilder.createCar();
            Model modelToSave = TestEntityBuilder.createModel();
            modelToSave.setCar(carToSave);
            brand.setModel(modelToSave);
            category.setModel(modelToSave);

            session.save(modelToSave);
            session.getTransaction().commit();

            assertThat(modelToSave.getId()).isNotNull();
            assertThat(carToSave.getId()).isNotNull();
            assertThat(modelToSave.getCars()).contains(carToSave);
            assertThat(carToSave.getModel().getId()).isEqualTo(modelToSave.getId());
        }
    }

    @Test
    void shouldReturnModel() {
        try (Session session = sessionFactory.openSession()) {
            Model expectedModel = ExistEntityBuilder.getExistModel();

            Model actualModel = session.find(Model.class, TEST_EXISTS_MODEL_ID);

            assertThat(actualModel).isNotNull();
            assertEquals(expectedModel, actualModel);
        }
    }

    @Test
    void shouldUpdateModel() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Model modelToUpdate = session.find(Model.class, TEST_EXISTS_MODEL_ID);
            Category category = session.get(Category.class, TEST_EXISTS_CATEGORY_ID);
            modelToUpdate.setEngineType(EngineType.ELECTRIC);
            modelToUpdate.setCategory(category);

            session.update(modelToUpdate);
            session.flush();
            session.evict(modelToUpdate);

            Model updatedModel = session.find(Model.class, modelToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedModel).isEqualTo(modelToUpdate);
        }
    }

    @Test
    void shouldDeleteModel() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Model modelToDelete = session.find(Model.class, TEST_MODEL_ID_FOR_DELETE);

            session.delete(modelToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Model.class, modelToDelete.getId())).isNull();
        }
    }
}