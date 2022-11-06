package integration.com.dmdev.repository;

import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.Model;
import com.dmdev.repository.CategoryRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CATEGORY_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CATEGORY_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryRepositoryTestIT extends IntegrationBaseTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldCreateCategory() {
        var categoryToSave = TestEntityBuilder.createCategory();

        var savedCategory = categoryRepository.save(categoryToSave);

        assertThat(savedCategory).isNotNull();
    }

    @Test
    void shouldCreateCategoryWithNotExistsModel() {
        var modelToSave = TestEntityBuilder.createModel();
        var categoryToSave = TestEntityBuilder.createCategory();
        categoryToSave.setModel(modelToSave);

        categoryRepository.save(categoryToSave);

        assertThat(categoryToSave.getId()).isNotNull();
        assertThat(modelToSave.getId()).isNotNull();
        assertThat(categoryToSave.getModels()).contains(modelToSave);
        assertThat(modelToSave.getCategory().getId()).isEqualTo(categoryToSave.getId());
    }

    @Test
    void shouldFindByIdCategory() {
        var expectedCategory = Optional.of(ExistEntityBuilder.getExistCategory());

        var actualCategory = categoryRepository.findById(TEST_EXISTS_CATEGORY_ID);

        assertThat(actualCategory).isNotNull();
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void shouldUpdateCategory() {
        var categoryToUpdate = categoryRepository.findById(TEST_EXISTS_CATEGORY_ID).get();
        categoryToUpdate.setPrice(BigDecimal.valueOf(90.00));
        categoryToUpdate.setName("test_name");

        categoryRepository.update(categoryToUpdate);

        var updatedCategory = categoryRepository.findById(categoryToUpdate.getId()).get();

        assertThat(updatedCategory).isEqualTo(categoryToUpdate);
    }

    @Test
    void shouldDeleteCategory() {
        var category = categoryRepository.findById(TEST_CATEGORY_ID_FOR_DELETE);

        category.ifPresent(ct -> categoryRepository.delete(ct));

        assertThat(categoryRepository.findById(TEST_CATEGORY_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllAccidents() {
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(2);

        List<String> names = categories.stream().map(Category::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder(
                "ECONOMY", "BUSINESS");
    }

    @Test
    void shouldReturnAllCategoriesWithQueryDsl() {
        List<Category> categories = categoryRepository.findAllQueryDsl();

        assertThat(categories).hasSize(2);
        assertThat(categories.get(0).getPrice()).isIn(BigDecimal.valueOf(50.00).setScale(2), BigDecimal.valueOf(100.00).setScale(2));
        assertThat(categories.get(1).getPrice()).isIn(BigDecimal.valueOf(50.00).setScale(2), BigDecimal.valueOf(100.00).setScale(2));

        List<String> modelNames = categories.stream()
                .map(Category::getModels)
                .flatMap(models ->
                        models.stream()
                                .map(Model::getName))
                .collect(toList());

        assertThat(modelNames).containsExactlyInAnyOrder("A8", "Benz");
    }

    @Test
    void shouldReturnCategoryBYIdWithQueryDsl() {
        var optionalCategory = categoryRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_CATEGORY_ID);

        assertThat(optionalCategory).isNotNull();
        optionalCategory.ifPresent(category -> assertThat(category.getId()).isEqualTo(ExistEntityBuilder.getExistCategory().getId()));
        assertThat(optionalCategory).isEqualTo(Optional.of(ExistEntityBuilder.getExistCategory()));
    }

    @Test
    void shouldReturnAllCategoriesByPriceWithQueryDsl() {
        List<Category> categories = categoryRepository.findCategoriesByPriceQueryDsl(BigDecimal.valueOf(100.00));

        assertThat(categories).hasSize(1);
        assertThat(categories.get(0).getName()).isEqualTo("BUSINESS");
    }

    @Test
    void shouldReturnAllCategoriesByPriceLessThanOrEqWithQueryDsl() {
        List<Category> categories = categoryRepository.findCategoriesByPriceLessThanQueryDsl(BigDecimal.valueOf(100.00));

        assertThat(categories).hasSize(2).contains(ExistEntityBuilder.getExistCategory());
    }

    @Test
    void shouldReturnAllCategoriesByNameCriterial() {
        List<Category> categories = categoryRepository.findCategoriesByNameCriteria("BUSINESS");

        assertThat(categories).hasSize(1);
        assertThat(categories.get(0)).isEqualTo(ExistEntityBuilder.getExistCategory());
    }
}