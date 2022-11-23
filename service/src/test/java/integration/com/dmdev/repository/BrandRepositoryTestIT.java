package integration.com.dmdev.repository;

import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.projection.BrandFullView;
import com.dmdev.domain.projection.CategoryView;
import com.dmdev.domain.projection.ModelView;
import com.dmdev.repository.BrandRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
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

        var savedBrand = brandRepository.saveAndFlush(brandToSave);

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

        brandRepository.saveAndFlush(brandToUpdate);

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
    void shouldFindByName() {
        var expectedBrand = Optional.of(ExistEntityBuilder.getExistBrand());
        var actualBrand = brandRepository.findByNameIgnoringCase("mercedes");
        assertThat(actualBrand).isPresent();
        assertEquals(expectedBrand, actualBrand);
    }

    @Test
    void shouldFindByNameIn() {
        var expectedBrand = ExistEntityBuilder.getExistBrand();
        List<Brand> actualBrands = brandRepository.findByNameInIgnoringCase(List.of("mercedes"));
        assertThat(actualBrands).hasSize(1);
        assertEquals(expectedBrand, actualBrands.get(0));
    }

    @Test
    void shouldFindByNameWithGraph() {
        var expectedBrand = Optional.of(ExistEntityBuilder.getExistBrand());
        var actualBrand = brandRepository.findByName("mercedes");
        assertThat(actualBrand).isPresent();
        assertEquals(expectedBrand, actualBrand);
    }

    @Test
    void shouldFindAllBrandFullView() {
        List<BrandFullView> brands = brandRepository.findAllFullView();
        assertThat(brands).hasSize(2);

        List<String> prices = brands.stream()
                .map(BrandFullView::getModels)
                .flatMap(model -> model.stream().map(ModelView::getName))
                .collect(toList());
        assertThat(prices).hasSize(2).containsExactlyInAnyOrder("A8", "Benz");
    }

    @Test
    void shouldFindByIdBrandFullView() {
        BrandFullView brand = brandRepository.findAllByIdFullView(TEST_EXISTS_BRAND_ID).get();

        assertThat(brand.getName()).isEqualTo("mercedes");
        assertThat(brand.getModels()).hasSize(1);
        assertThat(brand.getModels().get(0).getName()).isEqualTo("Benz");
    }

    @Test
    void shouldFindAllByNameBrandFullView() {
        List<BrandFullView> brands = brandRepository.findAllByNameFullView("mercedes");

        List<String> prices = brands.stream()
                .map(BrandFullView::getModels)
                .flatMap(model -> model.stream().map(ModelView::getName))
                .collect(toList());

        assertThat(prices).hasSize(1).containsExactlyInAnyOrder("Benz");
    }

    @Test
    void shouldFindAllByNameInBrandFullView() {
        List<BrandFullView> actualBrands = brandRepository.findAllByNameFullViewInIgnoringCase(List.of("mercedes", "reno"));

        assertThat(actualBrands).hasSize(1);

        List<String> prices = actualBrands.stream()
                .map(BrandFullView::getModels)
                .flatMap(model -> model.stream().map(ModelView::getName))
                .collect(toList());

        assertThat(prices).hasSize(1).containsExactlyInAnyOrder("Benz");
    }
}