package integration.com.dmdev.repository;


import com.dmdev.domain.dto.CarRentalTimeFilter;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.repository.CarRentalTimeRepository;
import com.dmdev.repository.OrderRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CAR_RENTAL_TIME_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CAR_RENTAL_TIME_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CarRentalTimeRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private CarRentalTimeRepository carRentalTimeRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldFindByIdCarRentalTime() {
        var expectedCarRentalTime = Optional.of(ExistEntityBuilder.getExistCarRentalTime());

        var actualCarRentalTime = carRentalTimeRepository.findById(TEST_EXISTS_CAR_RENTAL_TIME_ID);

        assertThat(actualCarRentalTime).isNotNull();
        assertEquals(expectedCarRentalTime, actualCarRentalTime);
    }

    @Test
    void shouldUpdateCarRentalTime() {
        var carRentalTimeToUpdate = carRentalTimeRepository.findById(TEST_EXISTS_CAR_RENTAL_TIME_ID).get();
        carRentalTimeToUpdate.setEndRentalDate(LocalDateTime.of(2022, 11, 9, 10, 0));

        carRentalTimeRepository.update(carRentalTimeToUpdate);

        var updatedCarRentalTime = carRentalTimeRepository.findById(carRentalTimeToUpdate.getId()).get();
        var updatedOrder = orderRepository.findById(carRentalTimeToUpdate.getOrder().getId()).get();

        assertThat(updatedCarRentalTime).isEqualTo(carRentalTimeToUpdate);
        assertThat(updatedOrder.getCarRentalTime()).isEqualTo(updatedCarRentalTime);
    }

    @Test
    void shouldDeleteCarRentalTime() {
        var carRentalTimeToDelete = carRentalTimeRepository.findById(TEST_CAR_RENTAL_TIME_ID_FOR_DELETE);

        carRentalTimeToDelete.ifPresent(crt -> crt.getOrder().setCarRentalTime(null));
        carRentalTimeToDelete.ifPresent(crt -> carRentalTimeRepository.delete(crt));

        assertThat(carRentalTimeRepository.findById(TEST_CAR_RENTAL_TIME_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllCarRentalTimes() {
        List<CarRentalTime> carRentalTimes = carRentalTimeRepository.findAll();
        assertThat(carRentalTimes).hasSize(2);

        List<LocalDateTime> startTimes = carRentalTimes.stream().map(CarRentalTime::getStartRentalDate).collect(toList());
        assertThat(startTimes).containsExactlyInAnyOrder(
                LocalDateTime.of(2022, 7, 2, 0, 0), LocalDateTime.of(2022, 9, 2, 0, 0));
    }

    @Test
    void shouldReturnAllCarRentalTimesWithQueryDsl() {
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
    }

    @Test
    void shouldReturnCarRentalTimeBYIdWithQueryDsl() {
        var optionalCarRentalTime = carRentalTimeRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_CAR_RENTAL_TIME_ID);

        assertThat(optionalCarRentalTime).isNotNull();
        optionalCarRentalTime.ifPresent(carRentalTime -> assertThat(carRentalTime.getId()).isEqualTo(ExistEntityBuilder.getExistCarRentalTime().getId()));
        assertThat(optionalCarRentalTime).isEqualTo(Optional.of(ExistEntityBuilder.getExistCarRentalTime()));
    }

    @Test
    void shouldReturnCarRentalTimeByOrderIdWithQueryDsl() {
        var optionalCarRentalTime = carRentalTimeRepository.findCarRentalTimesByOrderIdQueryDsl(TestEntityIdConst.TEST_EXISTS_ORDER_ID);

        assertThat(optionalCarRentalTime).isNotNull();
        optionalCarRentalTime.ifPresent(carRentalTime -> assertThat(carRentalTime.getId()).isEqualTo(ExistEntityBuilder.getExistCarRentalTime().getId()));
        assertThat(optionalCarRentalTime).isEqualTo(Optional.of(ExistEntityBuilder.getExistCarRentalTime()));
    }


    @Test
    void shouldReturnCarRentalTimesBetweenStartAndRentalDatesQueryDsl() {
        var carRentalTimeFilter = CarRentalTimeFilter.builder()
                .startRentalDate(LocalDateTime.of(2022, 7, 2, 0, 0, 0))
                .endRentalDate(LocalDateTime.of(2022, 9, 4, 23, 59, 0))
                .build();

        List<CarRentalTime> carRentalTimes = carRentalTimeRepository.findCarRentalTimesBetweenStartAndRentalDatesQueryDsl(carRentalTimeFilter);

        assertThat(carRentalTimes).hasSize(2).contains(ExistEntityBuilder.getExistCarRentalTime());
    }
}