package integration.com.dmdev.service;

import com.dmdev.domain.dto.model.ModelResponseDto;
import com.dmdev.service.BrandService;
import com.dmdev.service.ModelService;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.TestDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_MODEL_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class ModelServiceTestIT extends IntegrationBaseTest {

    private final ModelService modelService;
    private final BrandService brandService;

    @Test
    void shouldSaveModelCorrectly() {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());

        var actualModel = modelService.create(modelCreateRequestDto);

        assertTrue(actualModel.isPresent());
        assertEquals(modelCreateRequestDto.getName(), actualModel.get().getName());
        assertEquals(modelCreateRequestDto.getTransmission(), actualModel.get().getTransmission());
    }

    @Test
    void shouldFindAllModels() {
        var models = modelService.getAll();

        assertThat(models).hasSize(2);

        var names = models.stream().map(ModelResponseDto::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder("A8", "Benz");
    }

    @Test
    void shouldReturnModelById() {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var expectedModel = modelService.create(modelCreateRequestDto);

        var actualModel = modelService.getById(expectedModel.get().getId());

        assertThat(actualModel).isNotNull();
        assertEquals(expectedModel, actualModel);
    }

    @Test
    void shouldReturnModelByBrandId() {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var expectedModel = modelService.create(modelCreateRequestDto).get();

        var actualModel = modelService.getAllByBrandId(savedBrand.get().getId());

        assertThat(actualModel).isNotNull();
        assertThat(actualModel).hasSize(1);
        assertEquals(expectedModel, actualModel.get(0));
    }

    @Test
    void shouldUpdateCarCorrectly() {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);

        var modelUpdateRequestDto = TestDtoBuilder.createModelUpdateRequestDTO();
        var actualModel = modelService.update(savedModel.get().getId(), modelUpdateRequestDto);

        assertThat(actualModel).isNotNull();
        actualModel.ifPresent(model ->
                assertEquals(modelUpdateRequestDto.getEngineType(), model.getEngineType()));
    }

    @Test
    void shouldDeleteCarByIdCorrectly() {
        assertTrue(modelService.deleteById(TEST_MODEL_ID_FOR_DELETE));
    }

    @Test
    void shouldNotDeleteUserWithNonExistsId() {

        assertFalse(brandService.deleteById(999999L));
    }
}