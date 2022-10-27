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
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CAR_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CAR_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CarRepositoryTestIT extends IntegrationBaseTest {

    private final Session session = context.getBean(Session.class);
    private final CarRepository carRepository = context.getBean(CarRepository.class);
    private final ModelRepository modelRepository = context.getBean(ModelRepository.class);

    @Test
    void shouldSaveCar() {
        session.beginTransaction();
        var model = ExistEntityBuilder.getExistModel();
        var car = TestEntityBuilder.createCar();
        model.setCar(car);

        var savedCar = carRepository.save(car);

        assertThat(savedCar).isNotNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindByIdCar() {
        session.beginTransaction();
        var expectedCar = Optional.of(ExistEntityBuilder.getExistCar());

        var actualCar = carRepository.findById(TEST_EXISTS_CAR_ID);

        assertThat(actualCar).isNotNull();
        assertEquals(expectedCar, actualCar);
        session.getTransaction().rollback();
    }

    @Test
    void shouldUpdateCar() {
        session.beginTransaction();
        var carToUpdate = carRepository.findById(TEST_EXISTS_CAR_ID).get();
        var existModel = modelRepository.findById(1L).get();
        carToUpdate.setColor(Color.BLUE);
        carToUpdate.setYear(2010);
        carToUpdate.setModel(existModel);

        carRepository.update(carToUpdate);
        session.evict(carToUpdate);

        var updatedCar = carRepository.findById(carToUpdate.getId()).get();

        assertThat(updatedCar).isEqualTo(carToUpdate);
        session.getTransaction().rollback();
    }

    @Test
    void shouldDeleteCar() {
        session.beginTransaction();
        var car = carRepository.findById(TEST_CAR_ID_FOR_DELETE);
        car.ifPresent(cr -> carRepository.delete(cr));

        assertThat(carRepository.findById(TEST_CAR_ID_FOR_DELETE)).isEmpty();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindAllCars() {
        session.beginTransaction();

        List<Car> cars = carRepository.findAll();
        assertThat(cars).hasSize(2);

        List<String> carNumbers = cars.stream().map(Car::getCarNumber).collect(toList());
        assertThat(carNumbers).containsExactlyInAnyOrder(
                "7865AE-7", "7834AE-7");

        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllCarsWithQueryDsl() {
        session.beginTransaction();

        List<Car> cars = carRepository.findAllQueryDsl();

        assertThat(cars).hasSize(2);

        List<String> brandNames = cars.stream()
                .map(Car::getModel)
                .map(Model::getBrand)
                .map(Brand::getName)
                .collect(toList());

        assertThat(brandNames).containsExactlyInAnyOrder("audi", "mercedes");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnCarBYIdWithQueryDsl() {
        session.beginTransaction();

        var optionalCar = carRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_CAR_ID);

        assertThat(optionalCar).isNotNull();
        optionalCar.ifPresent(car -> assertThat(car.getId()).isEqualTo(ExistEntityBuilder.getExistCar().getId()));
        assertThat(optionalCar).isEqualTo(Optional.of(ExistEntityBuilder.getExistCar()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnCarByNumberCriteria() {
        session.beginTransaction();

        var optionalCar = carRepository.findCarByNumberCriteria("7834AE-7");

        assertThat(optionalCar).isNotNull();
        optionalCar.ifPresent(car -> assertThat(car.getId()).isEqualTo(ExistEntityBuilder.getExistCar().getId()));
        assertThat(optionalCar).isEqualTo(Optional.of(ExistEntityBuilder.getExistCar()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnCarByTransmissionGraph() {
        session.beginTransaction();

        List<Car> cars = carRepository.findCarByTransmissionGraph(Transmission.ROBOT);

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnCarsByColorAndYearOrGreateQueryDsl() {
        session.beginTransaction();
        var carFilter = CarFilter.builder()
                .color(Color.RED)
                .year(2022)
                .build();
        List<Car> cars = carRepository.findCarsByColorAndYearOrGreaterQueryDsl(carFilter);

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnCarsByBrandModelCategoryYearOrGreaterQueryDsl() {
        session.beginTransaction();
        var carFilter = CarFilter.builder()
                .brandName("mercedes")
                .modelName("Benz")
                .category("BUSINESS")
                .build();

        List<Car> cars = carRepository.findCarsByBrandModelCategoryYearOrGreaterQueryDsl(carFilter);

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
        session.getTransaction().rollback();
    }

    @Test
    void shouldNotReturnCarsByBrandModelCategoryYearOrGreaterQueryDsl() {
        session.beginTransaction();
        var carFilter = CarFilter.builder()
                .brandName("mercedes")
                .modelName("Benz")
                .category("dummy")
                .build();

        List<Car> cars = carRepository.findCarsByBrandModelCategoryYearOrGreaterQueryDsl(carFilter);

        assertThat(cars).isEmpty();
        session.getTransaction().rollback();
    }
}