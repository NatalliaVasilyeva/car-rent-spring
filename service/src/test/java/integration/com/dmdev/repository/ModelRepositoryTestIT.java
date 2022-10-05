package integration.com.dmdev.repository;

import com.dmdev.domain.dto.ModelFilter;
import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.Transmission;
import com.dmdev.repository.ModelRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class ModelRepositoryTestIT extends IntegrationBaseTest {

    private final ModelRepository modelRepository = ModelRepository.getInstance();

    @Test
    void shouldReturnAllModelsWithHql() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Model> models = modelRepository.findAllHQL(session);
            session.getTransaction().commit();

            assertThat(models).hasSize(2);

            List<String> modelNames = models.stream().map(Model::getName).collect(toList());
            assertThat(modelNames).contains("A8", "Benz");

            List<String> brands = models.stream().map(Model::getBrand).map(Brand::getName).collect(toList());
            assertThat(brands).contains("audi", "mercedes");
        }
    }

    @Test
    void shouldReturnAllModelsWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Model> models = modelRepository.findAllCriteria(session);
            session.getTransaction().commit();

            assertThat(models).hasSize(2);

            List<String> modelNames = models.stream().map(Model::getName).collect(toList());
            assertThat(modelNames).contains("A8", "Benz");

            List<String> brands = models.stream().map(Model::getBrand).map(Brand::getName).collect(toList());
            assertThat(brands).contains("audi", "mercedes");
        }
    }

    @Test
    void shouldReturnAllModelsWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Model> models = modelRepository.findAllQueryDsl(session);
            session.getTransaction().commit();

            assertThat(models).hasSize(2);

            List<String> modelNames = models.stream().map(Model::getName).collect(toList());
            assertThat(modelNames).contains("A8", "Benz");

            List<String> brands = models.stream().map(Model::getBrand).map(Brand::getName).collect(toList());
            assertThat(brands).contains("audi", "mercedes");
        }
    }

    @Test
    void shouldReturnModelByIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Model> optionalModel = modelRepository.findByIdCriteria(session, TestEntityIdConst.TEST_EXISTS_MODEL_ID);
            session.getTransaction().commit();

            assertThat(optionalModel).isNotNull();
            optionalModel.ifPresent(driverLicense -> assertThat(driverLicense).isEqualTo(ExistEntityBuilder.getExistModel()));
        }
    }

    @Test
    void shouldReturnModelByIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Model> optionalModel = modelRepository.findByIdQueryDsl(session, TestEntityIdConst.TEST_EXISTS_MODEL_ID);
            session.getTransaction().commit();

            assertThat(optionalModel).isNotNull();
            optionalModel.ifPresent(driverLicense -> assertThat(driverLicense).isEqualTo(ExistEntityBuilder.getExistModel()));
        }
    }

    @Test
    void shouldReturnModelsByModelAndBrandNameCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            ModelFilter modelFilter = ModelFilter.builder()
                    .brandName("mercedes")
                    .name("Benz")
                    .build();
            List<Model> models = modelRepository.findModelsByModelAndBrandNameCriteria(session, modelFilter);
            session.getTransaction().commit();

            assertThat(models).hasSize(1);
            assertThat(models.get(0)).isEqualTo(ExistEntityBuilder.getExistModel());
        }
    }

    @Test
    void shouldReturnModelsByBrandTransmissionEngineTypeOrderByBrandQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            ModelFilter modelFilter = ModelFilter.builder()
                    .brandName("mercedes")
                    .transmission(Transmission.ROBOT)
                    .engineType(EngineType.FUEL)
                    .build();
            List<Model> models = modelRepository.findModelsByBrandTransmissionEngineTypeOrderByBrandQueryDsl(session, modelFilter);
            session.getTransaction().commit();

            assertThat(models).hasSize(1);
            assertThat(models.get(0)).isEqualTo(ExistEntityBuilder.getExistModel());
        }
    }

    @Test
    void shouldNotReturnModelsByBrandTransmissionEngineTypeOrderByBrandQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            ModelFilter modelFilter = ModelFilter.builder()
                    .brandName("mercedes")
                    .transmission(Transmission.ROBOT)
                    .engineType(EngineType.DIESEL)
                    .build();
            List<Model> models = modelRepository.findModelsByBrandTransmissionEngineTypeOrderByBrandQueryDsl(session, modelFilter);
            session.getTransaction().commit();

            assertThat(models).isEmpty();
        }
    }

    @Test
    void shouldReturnModelsByBrandAndCategoryOrderByBrandQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            ModelFilter modelFilter = ModelFilter.builder()
                    .brandName("mercedes")
                    .categoryName("BUSINESS")
                    .build();
            List<Model> models = modelRepository.findModelsByBrandAndCategoryOrderByBrandQueryDsl(session, modelFilter);
            session.getTransaction().commit();

            assertThat(models).hasSize(1);
            assertThat(models.get(0)).isEqualTo(ExistEntityBuilder.getExistModel());
        }
    }


}