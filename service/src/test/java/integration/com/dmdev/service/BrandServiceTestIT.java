package integration.com.dmdev.service;

import com.dmdev.domain.dto.brand.request.BrandCreateEditRequestDto;
import com.dmdev.domain.dto.brand.response.BrandResponseDto;
import com.dmdev.service.BrandService;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.TestDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_BRAND_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class BrandServiceTestIT extends IntegrationBaseTest {

    private final BrandService brandService;

    @Test
    void shouldSaveBrandCorrectly() {
        var brandCreateRequestDTO = TestDtoBuilder.createBrandCreateEditRequestDto();
        var actualBrand = brandService.create(brandCreateRequestDTO);

        assertTrue(actualBrand.isPresent());
        assertEquals(brandCreateRequestDTO.getName(), actualBrand.get().getName());
    }

    @Test
    void shouldFindAllBrands() {
        var brands = brandService.getAll();

        assertThat(brands).hasSize(2);

        var names = brands.stream().map(BrandResponseDto::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder("audi", "mercedes");
    }

    @Test
    void shouldReturnBrandById() {
        var brandCreateRequestDTO = TestDtoBuilder.createBrandCreateEditRequestDto();
        var expectedBrands = brandService.create(brandCreateRequestDTO);

        var actualBrand = brandService.getById(expectedBrands.get().getId());

        assertThat(actualBrand).isNotNull();
        assertEquals(expectedBrands, actualBrand);
    }

    @Test
    void shouldReturnBrandByName() {
        var brandCreateRequestDTO = TestDtoBuilder.createBrandCreateEditRequestDto();
        var expectedBrands = brandService.create(brandCreateRequestDTO);

        var actualBrand = brandService.getByName(expectedBrands.get().getName());

        assertThat(actualBrand).isNotNull();
        assertEquals(expectedBrands, actualBrand);
    }

    @Test
    void shouldReturnBrandByNames() {
        var brandCreateRequestDTO = TestDtoBuilder.createBrandCreateEditRequestDto();
        brandService.create(brandCreateRequestDTO);

        var actualBrands = brandService.getByNames(List.of("a", "b"));

        assertThat(actualBrands).hasSize(0);
    }

    @Test
    void shouldUpdateBrandCorrectly() {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var brandUpdateRequestDto = new BrandCreateEditRequestDto(
                "toyotas");
        var savedBrand = brandService.create(brandCreateRequestDto).get();

        var actualBrand = brandService.update(savedBrand.getId(), brandUpdateRequestDto);

        assertThat(actualBrand).isNotNull();
        actualBrand.ifPresent(brand -> assertEquals(brandUpdateRequestDto.getName(), brand.getName()));
    }

    @Test
    void shouldDeleteBrandByIdCorrectly() {
        assertTrue(brandService.deleteById(TEST_BRAND_ID_FOR_DELETE));
    }

    @Test
    void shouldNotDeleteUserWithNonExistsId() {

        assertFalse(brandService.deleteById(999999L));
    }
}