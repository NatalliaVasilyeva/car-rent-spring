package integration.com.dmdev.repository;

import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.Model;
import com.dmdev.repository.CategoryRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CATEGORY_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CATEGORY_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryRepositoryTestIT extends IntegrationBaseTest {

    private final Session session = createProxySession(sessionFactory);
    private final CategoryRepository categoryRepository = new CategoryRepository(session);

    @Test
    void shouldCreateCategory() {
        session.beginTransaction();
        var categoryToSave = TestEntityBuilder.createCategory();

        var savedCategory = categoryRepository.save(categoryToSave);

        assertThat(savedCategory).isNotNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldCreateCategoryWithNotExistsModel() {
        session.beginTransaction();
        var modelToSave = TestEntityBuilder.createModel();
        var categoryToSave = TestEntityBuilder.createCategory();
        categoryToSave.setModel(modelToSave);

        categoryRepository.save(categoryToSave);

        assertThat(categoryToSave.getId()).isNotNull();
        assertThat(modelToSave.getId()).isNotNull();
        assertThat(categoryToSave.getModels()).contains(modelToSave);
        assertThat(modelToSave.getCategory().getId()).isEqualTo(categoryToSave.getId());
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindByIdCategory() {
        session.beginTransaction();
        var expectedCategory = Optional.of(ExistEntityBuilder.getExistCategory());

        var actualCategory = categoryRepository.findById(TEST_EXISTS_CATEGORY_ID);

        assertThat(actualCategory).isNotNull();
        assertEquals(expectedCategory, actualCategory);
        session.getTransaction().rollback();
    }

    @Test
    void shouldUpdateCategory() {
        session.beginTransaction();
        var categoryToUpdate = session.find(Category.class, TEST_EXISTS_CATEGORY_ID);
        categoryToUpdate.setPrice(BigDecimal.valueOf(90.00));
        categoryToUpdate.setName("test_name");

        categoryRepository.update(categoryToUpdate);
        session.evict(categoryToUpdate);

        var updatedCategory = session.find(Category.class, categoryToUpdate.getId());

        assertThat(updatedCategory).isEqualTo(categoryToUpdate);
        session.getTransaction().rollback();
    }

    @Test
    void shouldDeleteCategory() {
        session.beginTransaction();

        Category category = session.find(Category.class, TEST_CATEGORY_ID_FOR_DELETE);
        categoryRepository.delete(category);

        assertThat(session.find(Category.class, TEST_CATEGORY_ID_FOR_DELETE)).isNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindAllAccidents() {
        session.beginTransaction();

        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(2);

        List<String> names = categories.stream().map(Category::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder(
                "ECONOMY", "BUSINESS");

        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllCategoriesWithQueryDsl() {
        session.beginTransaction();

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
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnCategoryBYIdWithQueryDsl() {
        session.beginTransaction();

        Optional<Category> optionalCategory = categoryRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_CATEGORY_ID);

        assertThat(optionalCategory).isNotNull();
        optionalCategory.ifPresent(category -> assertThat(category.getId()).isEqualTo(ExistEntityBuilder.getExistCategory().getId()));
        assertThat(optionalCategory).isEqualTo(Optional.of(ExistEntityBuilder.getExistCategory()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllCategoriesByPriceWithQueryDsl() {
        session.beginTransaction();

        List<Category> categories = categoryRepository.findCategoriesByPriceQueryDsl(BigDecimal.valueOf(100.00));

        assertThat(categories).hasSize(1);
        assertThat(categories.get(0).getName()).isEqualTo("BUSINESS");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllCategoriesByPriceLessThanOrEqWithQueryDsl() {
        session.beginTransaction();

        List<Category> categories = categoryRepository.findCategoriesByPriceLessThanQueryDsl(BigDecimal.valueOf(100.00));

        assertThat(categories).hasSize(2).contains(ExistEntityBuilder.getExistCategory());
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllCategoriesByNameCriterial() {
        session.beginTransaction();

        List<Category> categories = categoryRepository.findCategoriesByNameCriteria("BUSINESS");

        assertThat(categories).hasSize(1);
        assertThat(categories.get(0)).isEqualTo(ExistEntityBuilder.getExistCategory());
        session.getTransaction().rollback();
    }
}