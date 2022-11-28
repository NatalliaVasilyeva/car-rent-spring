package integration.com.dmdev.service;

import com.dmdev.domain.dto.car.CarResponseDto;
import com.dmdev.service.BrandService;
import com.dmdev.service.CarService;
import com.dmdev.service.ModelService;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.TestDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CAR_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class CarServiceTestIT extends IntegrationBaseTest {

    private final CarService carService;
    private final ModelService modelService;
    private final BrandService brandService;

    @Test
    void shouldSaveCarCorrectly() {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());

        var actualCar = carService.create(carCreateRequestDto);

        assertTrue(actualCar.isPresent());
        assertEquals(carCreateRequestDto.getNumber(), actualCar.get().getNumber());
        assertEquals(carCreateRequestDto.getVin(), actualCar.get().getVin());
    }

    @Test
    void shouldFindAllCarsWithAccidents() {
        var cars = carService.getAllWithAccidents();

        assertThat(cars).hasSize(2);

        var brandNames = cars.stream().map(CarResponseDto::getBrand).collect(toList());
        assertThat(brandNames).containsExactlyInAnyOrder("audi", "mercedes");
    }

    @Test
    void shouldFindAllCarsWithoutAccidents() {
        var cars = carService.getAllWithoutAccidents();

        assertThat(cars).hasSize(0);
    }

    @Test
    void shouldFindAllAvailableCars() {
        var cars = carService.getAllAvailable();

        assertThat(cars).hasSize(2);

        var brandNames = cars.stream().map(CarResponseDto::getBrand).collect(toList());
        assertThat(brandNames).containsExactlyInAnyOrder("audi", "mercedes");
    }

    @Test
    void shouldFindAllCars() {
        var cars = carService.getAll();

        assertThat(cars).hasSize(2);

        var brandNames = cars.stream().map(CarResponseDto::getBrand).collect(toList());
        assertThat(brandNames).containsExactlyInAnyOrder("audi", "mercedes");
    }

    @Test
    void shouldReturnCarById() {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var expectedCar = carService.create(carCreateRequestDto);

        var actualCar = carService.getById(expectedCar.get().getId());

        assertThat(actualCar).isNotNull();
        assertEquals(expectedCar, actualCar);
    }

    @Test
    void shouldReturnCarByNumber() {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var expectedCar = carService.create(carCreateRequestDto);

        var actualCar = carService.getByCarNumber(expectedCar.get().getNumber());

        assertThat(actualCar).isNotNull();
        assertEquals(expectedCar, actualCar);
    }


    @Test
    void shouldUpdateCarCorrectly() {
        var brandCreateRequestDto = TestDtoBuilder.createBrandCreateEditRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelRequestDto(savedBrand.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var carCreateRequestDto = TestDtoBuilder.createCarRequestDto(savedBrand.get().getId(), savedModel.get().getId());
        var savedCar = carService.create(carCreateRequestDto);

        var carUpdateRequestDto = TestDtoBuilder.createCarUpdateRequestDTO(savedBrand.get().getId());
        var actualCar = carService.update(savedCar.get().getId(), carUpdateRequestDto);

        assertThat(actualCar).isNotNull();
        actualCar.ifPresent(car ->
                assertEquals(carUpdateRequestDto.getNumber(), car.getNumber()));
    }

    @Test
    void shouldDeleteCarByIdCorrectly() {
        assertTrue(carService.deleteById(TEST_CAR_ID_FOR_DELETE));
    }

    @Test
    void shouldNotDeleteUserWithNonExistsId() {

        assertFalse(brandService.deleteById(999999L));
    }
}