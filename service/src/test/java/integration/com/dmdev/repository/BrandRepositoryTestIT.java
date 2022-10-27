package integration.com.dmdev.repository;

import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Model;
import com.dmdev.repository.BrandRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_BRAND_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_BRAND_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BrandRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private BrandRepository brandRepository;

    @Test
    void shouldSaveBrand() {
        var brandToSave = TestEntityBuilder.createBrand();

        var savedBrand = brandRepository.save(brandToSave);

        assertNotNull(savedBrand.getId());
    }

    @Test
    void shouldFindByIdBrand() {
        var expectedBrand = Optional.of(ExistEntityBuilder.getExistBrand());

        var actualBrand = brandRepository.findById(TEST_EXISTS_BRAND_ID);

        assertThat(actualBrand).isNotNull();
        assertEquals(expectedBrand, actualBrand);
    }

    @Test
    void shouldUpdateBrand() {
        var brandToUpdate = brandRepository.findById(TEST_EXISTS_BRAND_ID).get();
        brandToUpdate.setName("pegas");

        brandRepository.update(brandToUpdate);

        var updatedBrand = brandRepository.findById(brandToUpdate.getId()).get();
        assertThat(updatedBrand).isEqualTo(brandToUpdate);
        assertEquals(brandToUpdate.getName(), updatedBrand.getModels().stream().map(Model::getBrand).findFirst().get().getName());
    }

    @Test
    void shouldDeleteBrand() {
        var brand = brandRepository.findById(TEST_BRAND_ID_FOR_DELETE);

        brand.ifPresent(br -> brandRepository.delete(br));

        assertThat(brandRepository.findById(TEST_BRAND_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        assertThat(brands).hasSize(2);

        List<String> names = brands.stream().map(Brand::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder("audi", "mercedes");
    }

    @Test
    void shouldReturnAllBrandsWithQueryDsl() {
        List<Brand> brands = brandRepository.findAllQueryDsl();

        assertThat(brands).hasSize(2);

        List<String> modelNames = brands.stream()
                .map(Brand::getModels)
                .flatMap(models ->
                        models.stream()
                                .map(Model::getName))
                .collect(toList());

        modelNames.forEach(System.out::println);

        assertThat(modelNames).containsExactlyInAnyOrder("A8", "Benz");
    }

    @Test
    void shouldReturnBrandBYIdWithQueryDsl() {

        var optionalBrand = brandRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_BRAND_ID);

        assertThat(optionalBrand).isNotNull();
        optionalBrand.ifPresent(brand -> assertThat(brand.getId()).isEqualTo(ExistEntityBuilder.getExistBrand().getId()));
        assertThat(optionalBrand).isEqualTo(Optional.of(ExistEntityBuilder.getExistBrand()));
    }

    @Test
    void shouldReturnBrandByNameWithQueryDsl() {
        var optionalBrand = brandRepository.findBrandByNameQueryDsl("mercedes");

        assertThat(optionalBrand).isNotNull();
        optionalBrand.ifPresent(brand -> assertThat(brand.getName()).isEqualTo("mercedes"));
    }
}