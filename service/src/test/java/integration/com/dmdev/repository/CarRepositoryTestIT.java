package integration.com.dmdev.repository;

import com.dmdev.domain.dto.CarFilter;
import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.model.Color;
import com.dmdev.domain.model.Transmission;
import com.dmdev.repository.CarRepository;
import com.dmdev.repository.ModelRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CAR_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CAR_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CarRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Test
    void shouldSaveCar() {
        var model = ExistEntityBuilder.getExistModel();
        var car = TestEntityBuilder.createCar();
        model.setCar(car);

        var savedCar = carRepository.save(car);

        assertThat(savedCar).isNotNull();
    }

    @Test
    void shouldFindByIdCar() {
        var expectedCar = Optional.of(ExistEntityBuilder.getExistCar());

        var actualCar = carRepository.findById(TEST_EXISTS_CAR_ID);

        assertThat(actualCar).isNotNull();
        assertEquals(expectedCar, actualCar);
    }

    @Test
    void shouldUpdateCar() {
        var carToUpdate = carRepository.findById(TEST_EXISTS_CAR_ID).get();
        var existModel = modelRepository.findById(1L).get();
        carToUpdate.setColor(Color.BLUE);
        carToUpdate.setYear(2010);
        carToUpdate.setModel(existModel);

        carRepository.update(carToUpdate);

        var updatedCar = carRepository.findById(carToUpdate.getId()).get();

        assertThat(updatedCar).isEqualTo(carToUpdate);
    }

    @Test
    void shouldDeleteCar() {
        var car = carRepository.findById(TEST_CAR_ID_FOR_DELETE);
        car.ifPresent(cr -> carRepository.delete(cr));

        assertThat(carRepository.findById(TEST_CAR_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllCars() {
        List<Car> cars = carRepository.findAll();
        assertThat(cars).hasSize(2);

        List<String> carNumbers = cars.stream().map(Car::getCarNumber).collect(toList());
        assertThat(carNumbers).containsExactlyInAnyOrder(
                "7865AE-7", "7834AE-7");
    }

    @Test
    void shouldReturnAllCarsWithQueryDsl() {
        List<Car> cars = carRepository.findAllQueryDsl();

        assertThat(cars).hasSize(2);

        List<String> brandNames = cars.stream()
                .map(Car::getModel)
                .map(Model::getBrand)
                .map(Brand::getName)
                .collect(toList());

        assertThat(brandNames).containsExactlyInAnyOrder("audi", "mercedes");
    }

    @Test
    void shouldReturnCarBYIdWithQueryDsl() {
        var optionalCar = carRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_CAR_ID);

        assertThat(optionalCar).isNotNull();
        optionalCar.ifPresent(car -> assertThat(car.getId()).isEqualTo(ExistEntityBuilder.getExistCar().getId()));
        assertThat(optionalCar).isEqualTo(Optional.of(ExistEntityBuilder.getExistCar()));
    }

    @Test
    void shouldReturnCarByNumberCriteria() {
        var optionalCar = carRepository.findCarByNumberCriteria("7834AE-7");

        assertThat(optionalCar).isNotNull();
        optionalCar.ifPresent(car -> assertThat(car.getId()).isEqualTo(ExistEntityBuilder.getExistCar().getId()));
        assertThat(optionalCar).isEqualTo(Optional.of(ExistEntityBuilder.getExistCar()));
    }

    @Test
    void shouldReturnCarByTransmissionGraph() {
        List<Car> cars = carRepository.findCarByTransmissionGraph(Transmission.ROBOT);

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
    }

    @Test
    void shouldReturnCarsByColorAndYearOrGreateQueryDsl() {

        var carFilter = CarFilter.builder()
                .color(Color.RED)
                .year(2022)
                .build();
        List<Car> cars = carRepository.findCarsByColorAndYearOrGreaterQueryDsl(carFilter);

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
    }

    @Test
    void shouldReturnCarsByBrandModelCategoryYearOrGreaterQueryDsl() {
        var carFilter = CarFilter.builder()
                .brandName("mercedes")
                .modelName("Benz")
                .category("BUSINESS")
                .build();

        List<Car> cars = carRepository.findCarsByBrandModelCategoryYearOrGreaterQueryDsl(carFilter);

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
    }

    @Test
    void shouldNotReturnCarsByBrandModelCategoryYearOrGreaterQueryDsl() {
        var carFilter = CarFilter.builder()
                .brandName("mercedes")
                .modelName("Benz")
                .category("dummy")
                .build();

        List<Car> cars = carRepository.findCarsByBrandModelCategoryYearOrGreaterQueryDsl(carFilter);

        assertThat(cars).isEmpty();
    }
}