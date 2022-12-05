package integration.com.dmdev.service;

import com.dmdev.domain.dto.carrentaltime.CarRentalTimeResponseDto;
import com.dmdev.domain.dto.carrentaltime.CarRentalTimeUpdateRequestDto;
import com.dmdev.service.CarRentalTimeService;
import integration.com.dmdev.IntegrationBaseTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CAR_RENTAL_TIME_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_CAR_RENTAL_TIME_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RequiredArgsConstructor
class CarRentalTimeServiceTestIT extends IntegrationBaseTest {

    private final CarRentalTimeService carRentalTimeService;

    @Test
    void shouldFindAllCarRentalTimes() {
        var carRentalTimes = carRentalTimeService.getAll();

        assertThat(carRentalTimes).hasSize(2);

        var startTimes = carRentalTimes.stream().map(CarRentalTimeResponseDto::getStartRentalDate).collect(toList());
        assertThat(startTimes).containsExactlyInAnyOrder(LocalDateTime.of(2022, 7, 2, 0, 0, 0), LocalDateTime.of(2022, 9, 2, 0, 0, 0));
    }

    @Test
    void shouldReturnCarRentalTimesById() {
        var actualCarRentalTime = carRentalTimeService.getById(TEST_EXISTS_CAR_RENTAL_TIME_ID);
        assertThat(actualCarRentalTime).isNotNull();
    }

    @Test
    void shouldUpdateCarRentalTimeCorrectly() {
        var carRentalTimeUpdateRequestDto = new CarRentalTimeUpdateRequestDto(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2));

        var existsCarRentalTime = carRentalTimeService.getById(TEST_EXISTS_CAR_RENTAL_TIME_ID).get();

        var actualCarRentalTime = carRentalTimeService.update(existsCarRentalTime.getId(), carRentalTimeUpdateRequestDto);

        assertThat(actualCarRentalTime).isNotNull();
        actualCarRentalTime.ifPresent(time -> assertEquals(carRentalTimeUpdateRequestDto.getEndRentalDate(), time.getEndRentalDate()));
    }

    @Test
    void shouldDeleteCategoryByIdCorrectly() {
        assertTrue(carRentalTimeService.deleteById(TEST_CAR_RENTAL_TIME_ID_FOR_DELETE));
    }

    @Test
    void shouldNotDeleteCategoryWithNonExistsId() {

        assertFalse(carRentalTimeService.deleteById(999999L));
    }
}