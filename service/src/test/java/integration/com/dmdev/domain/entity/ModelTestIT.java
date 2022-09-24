package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Model;
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

public class ModelTestIT extends IntegrationBaseTest {

    @Test
    public void shouldCreateModel() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedModelId = (Long) session.save(FakeTestEntityBuilder.createModel());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedModelId);
        }
    }

    @Test
    public void shouldReturnModel() {
        try (Session session = sessionFactory.openSession()) {
            Model actualModel = session.find(Model.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualModel).isNotNull();
            assertEquals(ExistTesEntityBuilder.getExistModel().getBrandId(), actualModel.getBrandId());
            assertEquals(ExistTesEntityBuilder.getExistModel().getCategoryId(), actualModel.getCategoryId());
            assertEquals(ExistTesEntityBuilder.getExistModel().getName(), actualModel.getName());
            assertEquals(ExistTesEntityBuilder.getExistModel().getEngineType(), actualModel.getEngineType());
            assertEquals(ExistTesEntityBuilder.getExistModel().getTransmission(), actualModel.getTransmission());
        }
    }

    @Test
    public void shouldUpdateModel() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Model modelToUpdate = ExistTesEntityBuilder.getUpdatedModel();
            session.update(modelToUpdate);
            session.getTransaction().commit();

            Model updatedModel = session.find(Model.class, modelToUpdate.getId());

            assertThat(updatedModel).isEqualTo(modelToUpdate);
        }
    }

    @Test
    public void shouldDeleteModel() {
        try (Session session = sessionFactory.openSession()) {
            Model modelToDelete = session.find(Model.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(modelToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Model.class, modelToDelete.getId())).isNull();
        }
    }
}