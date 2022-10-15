package integration.com.dmdev.repository;


import com.dmdev.domain.dto.CarRentalTimeFilter;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.repository.CarRentalTimeRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class CarRentalTimeRepositoryTestIT extends IntegrationBaseTest {

    private final CarRentalTimeRepository carRentalTimeRepository = CarRentalTimeRepository.getInstance();

    @Test
    void shouldReturnAllCarRentalTimesWithHql() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<CarRentalTime> carRentalTimes = carRentalTimeRepository.findAllHQL(session);
            session.getTransaction().commit();

            assertThat(carRentalTimes).hasSize(2);

            List<String> startCarRentTimes = carRentalTimes.stream()
                    .map(CarRentalTime::getStartRentalDate)
                    .map(LocalDateTime::toString)
                    .collect(toList());

            List<String> endCarRentTimes = carRentalTimes.stream()
                    .map(CarRentalTime::getEndRentalDate)
                    .map(LocalDateTime::toString)
                    .collect(toList());

            assertThat(startCarRentTimes).containsExactlyInAnyOrder("2022-07-02T00:00", "2022-09-02T00:00");
            assertThat(endCarRentTimes).containsExactlyInAnyOrder("2022-07-03T23:59", "2022-09-04T23:59");
        }
    }

    @Test
    void shouldReturnAllCarRentalTimesWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<CarRentalTime> carRentalTimes = carRentalTimeRepository.findAllCriteria(session);
            session.getTransaction().commit();

            assertThat(carRentalTimes).hasSize(2);

            List<String> startCarRentTimes = carRentalTimes.stream()
                    .map(CarRentalTime::getStartRentalDate)
                    .map(LocalDateTime::toString)
                    .collect(toList());

            List<String> endCarRentTimes = carRentalTimes.stream()
                    .map(CarRentalTime::getEndRentalDate)
                    .map(LocalDateTime::toString)
                    .collect(toList());

            assertThat(startCarRentTimes).containsExactlyInAnyOrder("2022-07-02T00:00", "2022-09-02T00:00");
            assertThat(endCarRentTimes).containsExactlyInAnyOrder("2022-07-03T23:59", "2022-09-04T23:59");
        }
    }

    @Test
    void shouldReturnAllCarRentalTimesWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<CarRentalTime> carRentalTimes = carRentalTimeRepository.findAllQueryDsl(session);
            session.getTransaction().commit();

            assertThat(carRentalTimes).hasSize(2);

            List<String> startCarRentTimes = carRentalTimes.stream()
                    .map(CarRentalTime::getStartRentalDate)
                    .map(LocalDateTime::toString)
                    .collect(toList());

            List<String> endCarRentTimes = carRentalTimes.stream()
                    .map(CarRentalTime::getEndRentalDate)
                    .map(LocalDateTime::toString)
                    .collect(toList());

            assertThat(startCarRentTimes).containsExactlyInAnyOrder("2022-07-02T00:00", "2022-09-02T00:00");
            assertThat(endCarRentTimes).containsExactlyInAnyOrder("2022-07-03T23:59", "2022-09-04T23:59");
        }
    }

    @Test
    void shouldReturnCarRentalTimeBYIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<CarRentalTime> optionalCarRentalTime = carRentalTimeRepository.findByIdCriteria(session, TestEntityIdConst.TEST_EXISTS_CAR_RENTAL_TIME_ID);
            session.getTransaction().commit();

            assertThat(optionalCarRentalTime).isNotNull();
            optionalCarRentalTime.ifPresent(carRentalTime -> assertThat(carRentalTime).isEqualTo(ExistEntityBuilder.getExistCarRentalTime()));

        }
    }

    @Test
    void shouldReturnCarRentalTimeBYIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<CarRentalTime> optionalCarRentalTime = carRentalTimeRepository.findByIdQueryDsl(session, TestEntityIdConst.TEST_EXISTS_CAR_RENTAL_TIME_ID);
            session.getTransaction().commit();

            assertThat(optionalCarRentalTime).isNotNull();
            optionalCarRentalTime.ifPresent(carRentalTime -> assertThat(carRentalTime).isEqualTo(ExistEntityBuilder.getExistCarRentalTime()));

        }
    }

    @Test
    void shouldReturnCarRentalTimeByOrderIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<CarRentalTime> optionalCarRentalTime = carRentalTimeRepository.findCarRentalTimesByOrderIdCriteria(session, TestEntityIdConst.TEST_EXISTS_ORDER_ID);
            session.getTransaction().commit();

            assertThat(optionalCarRentalTime).isNotNull();
            optionalCarRentalTime.ifPresent(carRentalTime -> assertThat(carRentalTime).isEqualTo(ExistEntityBuilder.getExistCarRentalTime()));

        }
    }

    @Test
    void shouldReturnCarRentalTimeByOrderIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<CarRentalTime> optionalCarRentalTime = carRentalTimeRepository.findCarRentalTimesByOrderIdQueryDsl(session, TestEntityIdConst.TEST_EXISTS_ORDER_ID);
            session.getTransaction().commit();

            assertThat(optionalCarRentalTime).isNotNull();
            optionalCarRentalTime.ifPresent(carRentalTime -> assertThat(carRentalTime).isEqualTo(ExistEntityBuilder.getExistCarRentalTime()));

        }
    }

    @Test
    void shouldReturnCarRentalTimesBetweenStartAndRentalDatesCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarRentalTimeFilter carRentalTimeFilter = CarRentalTimeFilter.builder()
                    .startRentalDate(LocalDateTime.of(2022, 7, 2, 0, 0,0))
                    .endRentalDate(LocalDateTime.of(2022, 9, 4, 23, 59,0))
                    .build();

            List<CarRentalTime> carRentalTimes = carRentalTimeRepository.findCarRentalTimesBetweenStartAndRentalDatesCriteria(session, carRentalTimeFilter);
            session.getTransaction().commit();

            assertThat(carRentalTimes).hasSize(2);
            assertThat(carRentalTimes).contains(ExistEntityBuilder.getExistCarRentalTime());

        }
    }

    @Test
    void shouldReturnCarRentalTimesBetweenStartAndRentalDatesQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarRentalTimeFilter carRentalTimeFilter = CarRentalTimeFilter.builder()
                    .startRentalDate(LocalDateTime.of(2022, 7, 2, 0, 0,0))
                    .endRentalDate(LocalDateTime.of(2022, 9, 4, 23, 59,0))
                    .build();

            List<CarRentalTime> carRentalTimes = carRentalTimeRepository.findCarRentalTimesBetweenStartAndRentalDatesQueryDsl(session, carRentalTimeFilter);
            session.getTransaction().commit();

            assertThat(carRentalTimes).hasSize(2);
            assertThat(carRentalTimes).contains(ExistEntityBuilder.getExistCarRentalTime());

        }
    }
}