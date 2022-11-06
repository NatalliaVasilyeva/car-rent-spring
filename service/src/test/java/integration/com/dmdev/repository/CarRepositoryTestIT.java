package integration.com.dmdev.repository;

import com.dmdev.domain.dto.filterdto.CarFilter;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.model.Color;
import com.dmdev.domain.model.Transmission;
import com.dmdev.repository.CarRepository;
import com.dmdev.repository.ModelRepository;
import com.dmdev.utils.predicate.CarPredicateBuilder;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CAR_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CAR_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private CarPredicateBuilder carPredicateBuilder;

    @Test
    void shouldSaveCar() {
        var model = ExistEntityBuilder.getExistModel();
        var car = TestEntityBuilder.createCar();
        model.setCar(car);

        var savedCar = carRepository.saveAndFlush(car);

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

        carRepository.saveAndFlush(carToUpdate);

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
    void shouldReturnCarByNumber() {
        var optionalCar = carRepository.findByCarNumber("7834AE-7");

        assertThat(optionalCar).isNotNull();
        optionalCar.ifPresent(car -> assertThat(car.getId()).isEqualTo(ExistEntityBuilder.getExistCar().getId()));
        assertThat(optionalCar).isEqualTo(Optional.of(ExistEntityBuilder.getExistCar()));
    }

    @Test
    void shouldReturnCarByNumberLike() {
        var cars = carRepository.findByCarNumberContainingIgnoreCase("834ae-7");

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0).getId()).isEqualTo(ExistEntityBuilder.getExistCar().getId());
        assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
    }

    @Test
    void shouldReturnCarByTransmission() {
        List<Car> cars = carRepository.findByTransmissionIgnoreCase(Transmission.ROBOT);

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
    }

    @Test
    void shouldReturnCarsByColorAndYearOrGreate() {

        var carFilter = CarFilter.builder()
                .color(Color.RED)
                .year(2022)
                .build();

        List<Car> cars = IterableUtils.toList(carRepository.findAll(carPredicateBuilder.build(carFilter)));

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
    }

    @Test
    void shouldReturnCarsByBrandModelCategoryYearOrGreater() {
        var carFilter = CarFilter.builder()
                .brandNames(List.of("mercedes"))
                .modelNames(List.of("Benz"))
                .categoryName("BUSINESS")
                .build();

        List<Car> cars = IterableUtils.toList(carRepository.findAll(carPredicateBuilder.build(carFilter)));

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
    }

    @Test
    void shouldNotReturnCarsByBrandModelCategoryYearOrGreater() {
        var carFilter = CarFilter.builder()
                .brandNames(List.of("mercedes"))
                .modelNames(List.of("Benz"))
                .categoryName("dummy")
                .build();

        List<Car> cars = IterableUtils.toList(carRepository.findAll(carPredicateBuilder.build(carFilter)));

        assertThat(cars).isEmpty();
    }

    @Test
    void shouldReturnCarsWithAccidents() {
        List<Car> cars = carRepository.findAllWithAccidents();

        assertThat(cars).hasSize(2);

        List<String> carNumbers = cars.stream().map(Car::getCarNumber).collect(toList());
        assertThat(carNumbers).containsExactlyInAnyOrder(
                "7865AE-7", "7834AE-7");
    }

    @Test
    void shouldReturnCarsWithoutAccidents() {
        List<Car> cars = carRepository.findAllWithoutAccidents();

        assertThat(cars).isEmpty();
    }

    @Test
    void shouldReturnAllAvailableCars() {
        List<Car> cars = carRepository.findAllAvailable();

        assertThat(cars).hasSize(2);

        List<String> carNumbers = cars.stream().map(Car::getCarNumber).collect(toList());
        assertThat(carNumbers).containsExactlyInAnyOrder(
                "7865AE-7", "7834AE-7");
    }

    @Test
    void shouldReturnAllCarsUnderRepaired() {
        List<Car> cars = carRepository.findAllUnderRepair();

        assertThat(cars).isEmpty();
    }

    @Test
    void shouldReturnIfCarNotAvailableForNecessaryDates() {
        var result = carRepository.isCarAvailable(TEST_EXISTS_CAR_ID, LocalDate.of(2022, 9, 3), LocalDate.of(2022, 9, 5));

        assertFalse(result);
    }

    @Test
    void shouldReturnIfCarAvailableForNecessaryDates() {
        var result = carRepository.isCarAvailable(TEST_EXISTS_CAR_ID, LocalDate.of(2022, 9, 5), LocalDate.of(2022, 9, 10));

        assertTrue(result);
    }
}