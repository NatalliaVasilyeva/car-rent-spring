package integration.com.dmdev.repository;

import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.Model;
import com.dmdev.repository.CategoryRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class CategoryRepositoryTestIT extends IntegrationBaseTest {

    private final CategoryRepository categoryRepository = CategoryRepository.getInstance();

    @Test
    void shouldReturnAllCategoriesWithHql() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Category> categories = categoryRepository.findAllHQL(session);
            session.getTransaction().commit();

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
    }

    @Test
    void shouldReturnAllCategoriesWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Category> categories = categoryRepository.findAllCriteria(session);
            session.getTransaction().commit();

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
    }

    @Test
    void shouldReturnAllCategoriesWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Category> categories = categoryRepository.findAllQueryDsl(session);
            session.getTransaction().commit();

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
    }

    @Test
    void shouldReturnCategoryBYIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Category> optionalCategory = categoryRepository.findByIdCriteria(session, TestEntityIdConst.TEST_EXISTS_CATEGORY_ID);
            session.getTransaction().commit();

            assertThat(optionalCategory).isNotNull();
            optionalCategory.ifPresent(category -> assertThat(category).isEqualTo(ExistEntityBuilder.getExistCategory()));
        }
    }

    @Test
    void shouldReturnCategoryBYIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Category> optionalCategory = categoryRepository.findByIdQueryDsl(session, TestEntityIdConst.TEST_EXISTS_CATEGORY_ID);
            session.getTransaction().commit();

            assertThat(optionalCategory).isNotNull();
            optionalCategory.ifPresent(category -> assertThat(category).isEqualTo(ExistEntityBuilder.getExistCategory()));
        }
    }

    @Test
    void shouldReturnAllCategoriesByPriceWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Category> categories = categoryRepository.findCategoriesByPriceCriteria(session, BigDecimal.valueOf(100.00));
            session.getTransaction().commit();

            assertThat(categories).hasSize(1);
            assertThat(categories.get(0).getName()).isEqualTo("BUSINESS");
        }
    }

    @Test
    void shouldReturnAllCategoriesByPriceWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Category> categories = categoryRepository.findCategoriesByPriceQueryDsl(session, BigDecimal.valueOf(100.00));
            session.getTransaction().commit();

            assertThat(categories).hasSize(1);
            assertThat(categories.get(0).getName()).isEqualTo("BUSINESS");
        }
    }

    @Test
    void shouldReturnAllCategoriesByPriceLessThanOrEqWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Category> categories = categoryRepository.findCategoriesByPriceLessThanQueryDsl(session, BigDecimal.valueOf(100.00));
            session.getTransaction().commit();

            assertThat(categories).hasSize(2);
            assertThat(categories).contains(ExistEntityBuilder.getExistCategory());
        }
    }

    @Test
    void shouldReturnAllCategoriesByNameCriterial() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Category> categories = categoryRepository.findCategoriesByNameCriteria(session, "BUSINESS");
            session.getTransaction().commit();

            assertThat(categories).hasSize(1);
            assertThat(categories.get(0)).isEqualTo(ExistEntityBuilder.getExistCategory());
        }
    }
}