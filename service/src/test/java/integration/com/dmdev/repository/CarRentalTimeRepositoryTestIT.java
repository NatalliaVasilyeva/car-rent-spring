package integration.com.dmdev.repository;


import com.dmdev.domain.dto.CarRentalTimeFilter;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.Order;
import com.dmdev.repository.BrandRepository;
import com.dmdev.repository.CarRentalTimeRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CAR_RENTAL_TIME_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CAR_RENTAL_TIME_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CarRentalTimeRepositoryTestIT extends IntegrationBaseTest {

    private final Session session = createProxySession(sessionFactory);
    private final CarRentalTimeRepository carRentalTimeRepository = new CarRentalTimeRepository(session);

    @Test
    void shouldFindByIdCarRentalTime() {
        session.beginTransaction();
        var expectedCarRentalTime = Optional.of(ExistEntityBuilder.getExistCarRentalTime());

        var actualCarRentalTime = carRentalTimeRepository.findById(TEST_EXISTS_CAR_RENTAL_TIME_ID);

        assertThat(actualCarRentalTime).isNotNull();
        assertEquals(expectedCarRentalTime, actualCarRentalTime);
        session.getTransaction().rollback();
    }

    @Test
    void shouldUpdateCarRentalTime() {
        session.beginTransaction();
        var carRentalTimeToUpdate = session.find(CarRentalTime.class, TEST_EXISTS_CAR_RENTAL_TIME_ID);
        carRentalTimeToUpdate.setEndRentalDate(LocalDateTime.of(2022, 11, 9, 10, 0));

        carRentalTimeRepository.update(carRentalTimeToUpdate);
        session.clear();

        var updatedCarRentalTime = session.find(CarRentalTime.class, carRentalTimeToUpdate.getId());
        var updatedOrder = session.find(Order.class, carRentalTimeToUpdate.getOrder().getId());

        assertThat(updatedCarRentalTime).isEqualTo(carRentalTimeToUpdate);
        assertThat(updatedOrder.getCarRentalTime()).isEqualTo(updatedCarRentalTime);
        session.getTransaction().rollback();
    }

    @Test
    void shouldDeleteCarRentalTime() {
        session.beginTransaction();
        var carRentalTimeToDelete = session.find(CarRentalTime.class, TEST_CAR_RENTAL_TIME_ID_FOR_DELETE);
        carRentalTimeToDelete.getOrder().setCarRentalTime(null);

        carRentalTimeRepository.delete(carRentalTimeToDelete);

        assertThat(session.find(CarRentalTime.class, TEST_CAR_RENTAL_TIME_ID_FOR_DELETE)).isNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindAllCarRentalTimes() {
        session.beginTransaction();

        List<CarRentalTime> carRentalTimes = carRentalTimeRepository.findAll();
        assertThat(carRentalTimes).hasSize(2);

        List<LocalDateTime> startTimes = carRentalTimes.stream().map(CarRentalTime::getStartRentalDate).collect(toList());
        assertThat(startTimes).containsExactlyInAnyOrder(
                LocalDateTime.of(2022, 7, 2, 0, 0), LocalDateTime.of(2022, 9, 2, 0, 0));

        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllCarRentalTimesWithQueryDsl() {
        session.beginTransaction();

        List<CarRentalTime> carRentalTimes = carRentalTimeRepository.findAllQueryDsl();

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
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnCarRentalTimeBYIdWithQueryDsl() {
        session.beginTransaction();

        Optional<CarRentalTime> optionalCarRentalTime = carRentalTimeRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_CAR_RENTAL_TIME_ID);

        assertThat(optionalCarRentalTime).isNotNull();
        optionalCarRentalTime.ifPresent(carRentalTime -> assertThat(carRentalTime.getId()).isEqualTo(ExistEntityBuilder.getExistCarRentalTime().getId()));
        assertThat(optionalCarRentalTime).isEqualTo(Optional.of(ExistEntityBuilder.getExistCarRentalTime()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnCarRentalTimeByOrderIdWithQueryDsl() {
        session.beginTransaction();

        Optional<CarRentalTime> optionalCarRentalTime = carRentalTimeRepository.findCarRentalTimesByOrderIdQueryDsl(TestEntityIdConst.TEST_EXISTS_ORDER_ID);

        assertThat(optionalCarRentalTime).isNotNull();
        optionalCarRentalTime.ifPresent(carRentalTime -> assertThat(carRentalTime.getId()).isEqualTo(ExistEntityBuilder.getExistCarRentalTime().getId()));
        assertThat(optionalCarRentalTime).isEqualTo(Optional.of(ExistEntityBuilder.getExistCarRentalTime()));
        session.getTransaction().rollback();
    }


    @Test
    void shouldReturnCarRentalTimesBetweenStartAndRentalDatesQueryDsl() {
        session.beginTransaction();
        CarRentalTimeFilter carRentalTimeFilter = CarRentalTimeFilter.builder()
                .startRentalDate(LocalDateTime.of(2022, 7, 2, 0, 0, 0))
                .endRentalDate(LocalDateTime.of(2022, 9, 4, 23, 59, 0))
                .build();

        List<CarRentalTime> carRentalTimes = carRentalTimeRepository.findCarRentalTimesBetweenStartAndRentalDatesQueryDsl(carRentalTimeFilter);

        assertThat(carRentalTimes).hasSize(2).contains(ExistEntityBuilder.getExistCarRentalTime());
        session.getTransaction().rollback();
    }
}