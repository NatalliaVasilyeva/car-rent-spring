package integration.com.dmdev.repository;

import com.dmdev.domain.entity.Category;
import com.dmdev.repository.CategoryRepository;
import integration.com.dmdev.IntegrationBaseTest;
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

        var savedCategory = categoryRepository.saveAndFlush(categoryToSave);

        assertThat(savedCategory).isNotNull();
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

        categoryRepository.saveAndFlush(categoryToUpdate);

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
    void shouldFindAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(2);

        List<String> names = categories.stream().map(Category::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder(
                "ECONOMY", "BUSINESS");
    }


    @Test
    void shouldReturnAllCategoriesByPrice() {
        List<Category> categories = categoryRepository.findAllByPrice(BigDecimal.valueOf(100.00));

        assertThat(categories).hasSize(1);
        assertThat(categories.get(0).getName()).isEqualTo("BUSINESS");
    }

    @Test
    void shouldReturnAllCategoriesByPriceLessThanOrEq() {
        List<Category> categories = categoryRepository.findAllByPriceLessThanEqual(BigDecimal.valueOf(100.00));

        assertThat(categories).hasSize(2).contains(ExistEntityBuilder.getExistCategory());
    }

    @Test
    void shouldReturnAllCategoriesByPriceGreaterThanOrEq() {
        List<Category> categories = categoryRepository.findAllByPriceGreaterThanEqual(BigDecimal.valueOf(101.00));

        assertThat(categories).isEmpty();
    }

    @Test
    void shouldReturnAllCategoriesByNameIgnoreCase() {
        Optional<Category> category = categoryRepository.findByNameIgnoringCase("business");

        assertThat(category).isPresent();
        category.ifPresent(ct -> ct.getName().equalsIgnoreCase("business"));
    }
}