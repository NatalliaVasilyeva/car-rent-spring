package integration.com.dmdev.repository;

import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Model;
import com.dmdev.repository.BrandRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class BrandRepositoryTestIT extends IntegrationBaseTest {

    private final BrandRepository brandRepository = BrandRepository.getInstance();

    @Test
    void shouldReturnAllBrandsWithHql() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Brand> brands = brandRepository.findAllHql(session);

            assertThat(brands).hasSize(2);

            List<String> modelNames = brands.stream()
                    .map(Brand::getModels)
                    .flatMap(models ->
                            models.stream()
                                    .map(Model::getName))
                    .collect(toList());

            modelNames.forEach(System.out::println);

            assertThat(modelNames).containsExactlyInAnyOrder("A8", "Benz");
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnAllBrandsWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Brand> brands = brandRepository.findAllCriteria(session);

            assertThat(brands).hasSize(2);

            List<String> modelNames = brands.stream()
                    .map(Brand::getModels)
                    .flatMap(models ->
                            models.stream()
                                    .map(Model::getName))
                    .collect(toList());

            modelNames.forEach(System.out::println);

            assertThat(modelNames).containsExactlyInAnyOrder("A8", "Benz");
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnAllBrandsWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Brand> brands = brandRepository.findAllQueryDsl(session);

            assertThat(brands).hasSize(2);

            List<String> modelNames = brands.stream()
                    .map(Brand::getModels)
                    .flatMap(models ->
                            models.stream()
                                    .map(Model::getName))
                    .collect(toList());

            modelNames.forEach(System.out::println);

            assertThat(modelNames).containsExactlyInAnyOrder("A8", "Benz");
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnBrandBYIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Brand> optionalBrand = brandRepository.findByIdCriteria(session, TestEntityIdConst.TEST_EXISTS_BRAND_ID);

            assertThat(optionalBrand).isNotNull();
            optionalBrand.ifPresent(brand -> assertThat(brand.getId()).isEqualTo(ExistEntityBuilder.getExistBrand().getId()));
            assertThat(optionalBrand).isEqualTo(Optional.of(ExistEntityBuilder.getExistBrand()));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnBrandBYIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Brand> optionalBrand = brandRepository.findByIdQueryDsl(session, TestEntityIdConst.TEST_EXISTS_BRAND_ID);

            assertThat(optionalBrand).isNotNull();
            optionalBrand.ifPresent(brand -> assertThat(brand.getId()).isEqualTo(ExistEntityBuilder.getExistBrand().getId()));
            assertThat(optionalBrand).isEqualTo(Optional.of(ExistEntityBuilder.getExistBrand()));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnBrandByNameWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Brand> optionalBrand = brandRepository.findBrandByNameCriteria(session, "audi");

            assertThat(optionalBrand).isNotNull();
            optionalBrand.ifPresent(brand -> assertThat(brand.getName()).isEqualTo("audi"));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnBrandByNameWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Brand> optionalBrand = brandRepository.findBrandByNameQueryDsl(session, "mercedes");

            assertThat(optionalBrand).isNotNull();
            optionalBrand.ifPresent(brand -> assertThat(brand.getName()).isEqualTo("mercedes"));
            session.getTransaction().rollback();
        }
    }
}