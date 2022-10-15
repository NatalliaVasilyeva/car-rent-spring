package integration.com.dmdev.repository;

import com.dmdev.domain.dto.CarFilter;
import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.model.Color;
import com.dmdev.domain.model.Transmission;
import com.dmdev.repository.CarRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class CarRepositoryTestIT extends IntegrationBaseTest {

    private final CarRepository carRepository = CarRepository.getInstance();

    @Test
    void shouldReturnAllCarsWithHql() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Car> cars = carRepository.findAllHql(session);

            assertThat(cars).hasSize(2);

            List<String> brandNames = cars.stream()
                    .map(Car::getModel)
                    .map(Model::getBrand)
                    .map(Brand::getName)
                    .collect(toList());

            assertThat(brandNames).containsExactlyInAnyOrder("audi", "mercedes");
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnAllCarsWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Car> cars = carRepository.findAllCriteria(session);

            assertThat(cars).hasSize(2);

            List<String> brandNames = cars.stream()
                    .map(Car::getModel)
                    .map(Model::getBrand)
                    .map(Brand::getName)
                    .collect(toList());

            assertThat(brandNames).containsExactlyInAnyOrder("audi", "mercedes");
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnAllCarsWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Car> cars = carRepository.findAllQueryDsl(session);

            assertThat(cars).hasSize(2);

            List<String> brandNames = cars.stream()
                    .map(Car::getModel)
                    .map(Model::getBrand)
                    .map(Brand::getName)
                    .collect(toList());

            assertThat(brandNames).containsExactlyInAnyOrder("audi", "mercedes");
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnCarBYIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Car> optionalCar = carRepository.findByIdCriteria(session, TestEntityIdConst.TEST_EXISTS_CAR_ID);

            assertThat(optionalCar).isNotNull();
            optionalCar.ifPresent(car -> assertThat(car.getId()).isEqualTo(ExistEntityBuilder.getExistCar().getId()));
            assertThat(optionalCar).isEqualTo(Optional.of(ExistEntityBuilder.getExistCar()));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnCarBYIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Car> optionalCar = carRepository.findByIdQueryDsl(session, TestEntityIdConst.TEST_EXISTS_CAR_ID);

            assertThat(optionalCar).isNotNull();
            optionalCar.ifPresent(car -> assertThat(car.getId()).isEqualTo(ExistEntityBuilder.getExistCar().getId()));
            assertThat(optionalCar).isEqualTo(Optional.of(ExistEntityBuilder.getExistCar()));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnCarByNumberCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Car> optionalCar = carRepository.findCarByNumberCriteria(session, "7834AE-7");

            assertThat(optionalCar).isNotNull();
            optionalCar.ifPresent(car -> assertThat(car.getId()).isEqualTo(ExistEntityBuilder.getExistCar().getId()));
            assertThat(optionalCar).isEqualTo(Optional.of(ExistEntityBuilder.getExistCar()));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnCarByTransmissionGraph() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Car> cars = carRepository.findCarByTransmissionGraph(session, Transmission.ROBOT);

            assertThat(cars).hasSize(1);
            assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnCarsByColorAndYearCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarFilter carFilter = CarFilter.builder()
                    .color(Color.RED)
                    .year(2022)
                    .build();
            List<Car> cars = carRepository.findCarsByColorAndYearOrGreaterCriteria(session, carFilter);

            assertThat(cars).hasSize(1);
            assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldNotReturnAnyCarsByColorAndYearOrGreateCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarFilter carFilter = CarFilter.builder()
                    .color(Color.RED)
                    .year(2023)
                    .build();
            List<Car> cars = carRepository.findCarsByColorAndYearOrGreaterCriteria(session, carFilter);

            assertThat(cars).isEmpty();
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnCarsByColorAndYearOrGreateQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarFilter carFilter = CarFilter.builder()
                    .color(Color.RED)
                    .year(2022)
                    .build();
            List<Car> cars = carRepository.findCarsByColorAndYearOrGreaterQueryDsl(session, carFilter);

            assertThat(cars).hasSize(1);
            assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnCarsByBrandModelCategoryYearOrGreaterQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarFilter carFilter = CarFilter.builder()
                    .brandName("mercedes")
                    .modelName("Benz")
                    .category("BUSINESS")
                    .build();
            List<Car> cars = carRepository.findCarsByBrandModelCategoryYearOrGreaterQueryDsl(session, carFilter);

            assertThat(cars).hasSize(1);
            assertThat(cars.get(0)).isEqualTo(ExistEntityBuilder.getExistCar());
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldNotReturnCarsByBrandModelCategoryYearOrGreaterQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarFilter carFilter = CarFilter.builder()
                    .brandName("mercedes")
                    .modelName("Benz")
                    .category("dummy")
                    .build();
            List<Car> cars = carRepository.findCarsByBrandModelCategoryYearOrGreaterQueryDsl(session, carFilter);

            assertThat(cars).isEmpty();
            session.getTransaction().rollback();
        }
    }
}