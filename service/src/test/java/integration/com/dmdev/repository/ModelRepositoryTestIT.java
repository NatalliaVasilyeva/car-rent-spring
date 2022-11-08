package integration.com.dmdev.repository;

import com.dmdev.domain.dto.filterdto.ModelFilter;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.Transmission;
import com.dmdev.repository.BrandRepository;
import com.dmdev.repository.CategoryRepository;
import com.dmdev.repository.ModelRepository;
import com.dmdev.utils.predicate.ModelPredicateBuilder;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

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

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelPredicateBuilder modelPredicateBuilder;

    @Test
    void shouldSaveModel() {
        var brand = brandRepository.findById(TEST_EXISTS_BRAND_ID).get();
        var category = categoryRepository.findById(TEST_EXISTS_CATEGORY_ID).get();
        var modelToSave = TestEntityBuilder.createModel();
        brand.setModel(modelToSave);
        category.setModel(modelToSave);

        var savedModel = modelRepository.saveAndFlush(modelToSave);

        assertThat(savedModel).isNotNull();
    }

    @Test
    void shouldCreateModelWithNotExistsCar() {
        var brand = brandRepository.findById(TEST_EXISTS_BRAND_ID).get();
        var category = categoryRepository.findById(TEST_EXISTS_CATEGORY_ID).get();
        var carToSave = TestEntityBuilder.createCar();
        var modelToSave = TestEntityBuilder.createModel();
        modelToSave.setCar(carToSave);
        brand.setModel(modelToSave);
        category.setModel(modelToSave);

        modelRepository.saveAndFlush(modelToSave);

        assertThat(modelToSave.getId()).isNotNull();
        assertThat(carToSave.getId()).isNotNull();
        assertThat(modelToSave.getCars()).contains(carToSave);
        assertThat(carToSave.getModel().getId()).isEqualTo(modelToSave.getId());
    }

    @Test
    void shouldFindByIdModel() {
        var expectedModel = Optional.of(ExistEntityBuilder.getExistModel());

        var actualModel = modelRepository.findById(TEST_EXISTS_MODEL_ID);

        assertThat(actualModel).isNotNull();
        assertEquals(expectedModel, actualModel);
    }

    @Test
    void shouldUpdateModel() {
        var modelToUpdate = modelRepository.findById(TEST_EXISTS_MODEL_ID).get();
        var category = categoryRepository.findById(TEST_EXISTS_CATEGORY_ID).get();
        modelToUpdate.setEngineType(EngineType.ELECTRIC);
        modelToUpdate.setCategory(category);

        modelRepository.saveAndFlush(modelToUpdate);

        var updatedModel = modelRepository.findById(modelToUpdate.getId()).get();

        assertThat(updatedModel).isEqualTo(modelToUpdate);
    }

    @Test
    void shouldDeleteModel() {
        var model = modelRepository.findById(TEST_MODEL_ID_FOR_DELETE);

        model.ifPresent(md -> modelRepository.delete(md));

        assertThat(modelRepository.findById(TEST_MODEL_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllModels() {
        List<Model> models = modelRepository.findAll();
        assertThat(models).hasSize(2);

        List<String> names = models.stream().map(Model::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder("A8", "Benz");
    }

    @Test
    void shouldFindAllModelsByName() {
        List<Model> models = modelRepository.findModelsByName("Benz");
        assertThat(models).hasSize(1);

        assertThat(models.get(0).getBrand().getName()).isEqualTo("mercedes");
        assertThat(models.get(0).getTransmission()).isEqualTo(Transmission.ROBOT);
    }

    @Test
    void shouldFindAllModelsByBrandName() {
        List<Model> models = modelRepository.findModelsByBrandName("mercedes");
        assertThat(models).hasSize(1);

        assertThat(models.get(0).getName()).isEqualTo("Benz");
        assertThat(models.get(0).getTransmission()).isEqualTo(Transmission.ROBOT);
    }

    @Test
    void shouldFindAllModelsByBrandId() {
        List<Model> models = modelRepository.findModelsByBrandId(TEST_EXISTS_BRAND_ID);
        assertThat(models).hasSize(1);

        assertThat(models.get(0).getName()).isEqualTo("Benz");
        assertThat(models.get(0).getTransmission()).isEqualTo(Transmission.ROBOT);
    }


    @Test
    void shouldReturnModelsByFilterWithModelAndBrandName() {
        var modelFilter = ModelFilter.builder()
                .brands(List.of("mercedes"))
                .models(List.of("Benz"))
                .build();

        Iterable models = modelRepository.findAll(modelPredicateBuilder.build(modelFilter));

        assertThat(models).hasSize(1);
        assertThat(models.iterator().next()).isEqualTo(ExistEntityBuilder.getExistModel());
    }

    @Test
    void shouldReturnModelsByFilterWithBrandTransmissionEngineTypeOrderByBrand() {
        var modelFilter = ModelFilter.builder()
                .brands(List.of("mercedes"))
                .transmission(Transmission.ROBOT)
                .engineType(EngineType.FUEL)
                .build();

        Sort sort = Sort.by("brand").descending();
        Iterable models = modelRepository.findAll(modelPredicateBuilder.build(modelFilter), sort);

        assertThat(models).hasSize(1);
        assertThat(models.iterator().next()).isEqualTo(ExistEntityBuilder.getExistModel());
    }

    @Test
    void shouldNotReturnModelsByByFilterWithBrandTransmissionEngineTypeOrderByBrand() {
        var modelFilter = ModelFilter.builder()
                .brands(List.of("mercedes"))
                .transmission(Transmission.ROBOT)
                .engineType(EngineType.DIESEL)
                .build();

        Sort sort = Sort.by("brand").descending();
        Iterable models = modelRepository.findAll(modelPredicateBuilder.build(modelFilter), sort);

        assertThat(models).isEmpty();
    }

    @Test
    void shouldReturnModelsByBrandAndCategoryOrderByBrand() {
        var modelFilter = ModelFilter.builder()
                .brands(List.of("mercedes"))
                .categories(List.of("BUSINESS"))
                .build();

        Sort sort = Sort.by("brand").descending();
        Iterable models = modelRepository.findAll(modelPredicateBuilder.build(modelFilter), sort);

        assertThat(models).hasSize(1);
        assertThat(models.iterator().next()).isEqualTo(ExistEntityBuilder.getExistModel());
    }
}