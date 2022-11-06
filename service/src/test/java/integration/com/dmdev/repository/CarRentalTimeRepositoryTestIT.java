package integration.com.dmdev.repository;


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

        carRentalTimeRepository.save(carRentalTimeToUpdate);

        var updatedCarRentalTime = carRentalTimeRepository.findById(carRentalTimeToUpdate.getId()).get();
        var updatedOrder = orderRepository.findById(carRentalTimeToUpdate.getOrder().getId()).get();

        assertThat(updatedCarRentalTime).isEqualTo(carRentalTimeToUpdate);
        assertThat(updatedOrder.getCarRentalTime()).isEqualTo(updatedCarRentalTime);
    }

    @Test
    void shouldDeleteCarRentalTime() {
        var carRentalTimeToDelete = carRentalTimeRepository.findById(TEST_CAR_RENTAL_TIME_ID_FOR_DELETE);

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
    void shouldReturnCarRentalTimeByOrderId() {
        var optionalCarRentalTime = carRentalTimeRepository.findByOrderId(TestEntityIdConst.TEST_EXISTS_ORDER_ID);

        assertThat(optionalCarRentalTime).isNotNull();
        optionalCarRentalTime.ifPresent(carRentalTime -> assertThat(carRentalTime.getId()).isEqualTo(ExistEntityBuilder.getExistCarRentalTime().getId()));
        assertThat(optionalCarRentalTime).isEqualTo(Optional.of(ExistEntityBuilder.getExistCarRentalTime()));
    }
}