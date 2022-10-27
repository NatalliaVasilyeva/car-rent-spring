package integration.com.dmdev.repository;

import com.dmdev.domain.dto.ModelFilter;
import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.Transmission;
import com.dmdev.repository.BrandRepository;
import com.dmdev.repository.CategoryRepository;
import com.dmdev.repository.ModelRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_BRAND_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CATEGORY_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_MODEL_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_MODEL_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelRepositoryTestIT extends IntegrationBaseTest {

    private final Session session = context.getBean(Session.class);
    private final ModelRepository modelRepository = context.getBean(ModelRepository.class);
    private final BrandRepository brandRepository = context.getBean(BrandRepository.class);
    private final CategoryRepository categoryRepository = context.getBean(CategoryRepository.class);

    @Test
    void shouldSaveModel() {
        session.beginTransaction();
        var brand = brandRepository.findById(TEST_EXISTS_BRAND_ID).get();
        var category = categoryRepository.findById(TEST_EXISTS_CATEGORY_ID).get();
        var modelToSave = TestEntityBuilder.createModel();
        brand.setModel(modelToSave);
        category.setModel(modelToSave);

        var savedModel = modelRepository.save(modelToSave);

        assertThat(savedModel).isNotNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldCreateModelWithNotExistsCar() {
        session.beginTransaction();
        var brand = brandRepository.findById(TEST_EXISTS_BRAND_ID).get();
        var category = categoryRepository.findById(TEST_EXISTS_CATEGORY_ID).get();
        var carToSave = TestEntityBuilder.createCar();
        var modelToSave = TestEntityBuilder.createModel();
        modelToSave.setCar(carToSave);
        brand.setModel(modelToSave);
        category.setModel(modelToSave);

        modelRepository.save(modelToSave);

        assertThat(modelToSave.getId()).isNotNull();
        assertThat(carToSave.getId()).isNotNull();
        assertThat(modelToSave.getCars()).contains(carToSave);
        assertThat(carToSave.getModel().getId()).isEqualTo(modelToSave.getId());
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindByIdModel() {
        session.beginTransaction();
        var expectedModel = Optional.of(ExistEntityBuilder.getExistModel());

        var actualModel = modelRepository.findById(TEST_EXISTS_MODEL_ID);

        assertThat(actualModel).isNotNull();
        assertEquals(expectedModel, actualModel);
        session.getTransaction().rollback();
    }

    @Test
    void shouldUpdateModel() {
        session.beginTransaction();
        var modelToUpdate = modelRepository.findById(TEST_EXISTS_MODEL_ID).get();
        var category = categoryRepository.findById(TEST_EXISTS_CATEGORY_ID).get();
        modelToUpdate.setEngineType(EngineType.ELECTRIC);
        modelToUpdate.setCategory(category);

        modelRepository.update(modelToUpdate);
        session.evict(modelToUpdate);

        var updatedModel = modelRepository.findById(modelToUpdate.getId()).get();

        assertThat(updatedModel).isEqualTo(modelToUpdate);
        session.getTransaction().rollback();
    }

    @Test
    void shouldDeleteModel() {
        session.beginTransaction();

        var model = modelRepository.findById(TEST_MODEL_ID_FOR_DELETE);
        model.ifPresent(md -> modelRepository.delete(md));

        assertThat(modelRepository.findById(TEST_MODEL_ID_FOR_DELETE)).isEmpty();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindAllModels() {
        session.beginTransaction();

        List<Model> models = modelRepository.findAll();
        assertThat(models).hasSize(2);

        List<String> names = models.stream().map(Model::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder("A8", "Benz");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllModelsWithQueryDsl() {
        session.beginTransaction();

        List<Model> models = modelRepository.findAllQueryDsl();
        assertThat(models).hasSize(2);

        List<String> modelNames = models.stream().map(Model::getName).collect(toList());
        assertThat(modelNames).contains("A8", "Benz");

        List<String> brands = models.stream().map(Model::getBrand).map(Brand::getName).collect(toList());
        assertThat(brands).contains("audi", "mercedes");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnModelByIdWithQueryDsl() {
        session.beginTransaction();

        var optionalModel = modelRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_MODEL_ID);

        assertThat(optionalModel).isNotNull();
        optionalModel.ifPresent(model -> assertThat(model.getId()).isEqualTo(ExistEntityBuilder.getExistModel().getId()));
        assertThat(optionalModel).isEqualTo(Optional.of(ExistEntityBuilder.getExistModel()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnModelsByModelAndBrandNameCriteria() {
        session.beginTransaction();
        var modelFilter = ModelFilter.builder()
                .brandName("mercedes")
                .name("Benz")
                .build();

        List<Model> models = modelRepository.findModelsByModelAndBrandNameCriteria(modelFilter);

        assertThat(models).hasSize(1);
        assertThat(models.get(0)).isEqualTo(ExistEntityBuilder.getExistModel());
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnModelsByBrandTransmissionEngineTypeOrderByBrandQueryDsl() {
        session.beginTransaction();
        var modelFilter = ModelFilter.builder()
                .brandName("mercedes")
                .transmission(Transmission.ROBOT)
                .engineType(EngineType.FUEL)
                .build();

        List<Model> models = modelRepository.findModelsByBrandTransmissionEngineTypeOrderByBrandQueryDsl(modelFilter);

        assertThat(models).hasSize(1);
        assertThat(models.get(0)).isEqualTo(ExistEntityBuilder.getExistModel());
        session.getTransaction().rollback();
    }

    @Test
    void shouldNotReturnModelsByBrandTransmissionEngineTypeOrderByBrandQueryDsl() {
        session.beginTransaction();
        var modelFilter = ModelFilter.builder()
                .brandName("mercedes")
                .transmission(Transmission.ROBOT)
                .engineType(EngineType.DIESEL)
                .build();

        List<Model> models = modelRepository.findModelsByBrandTransmissionEngineTypeOrderByBrandQueryDsl(modelFilter);

        assertThat(models).isEmpty();
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnModelsByBrandAndCategoryOrderByBrandQueryDsl() {
        session.beginTransaction();
        var modelFilter = ModelFilter.builder()
                .brandName("mercedes")
                .categoryName("BUSINESS")
                .build();

        List<Model> models = modelRepository.findModelsByBrandAndCategoryOrderByBrandQueryDsl(modelFilter);

        assertThat(models).hasSize(1);
        assertThat(models.get(0)).isEqualTo(ExistEntityBuilder.getExistModel());
        session.getTransaction().rollback();
    }
}