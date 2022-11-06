package integration.com.dmdev.repository;

import com.dmdev.domain.dto.AccidentFilter;
import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.Order;
import com.dmdev.repository.AccidentRepository;
import com.dmdev.repository.OrderRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_ACCIDENT_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_ACCIDENT_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_ORDER_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccidentRepositoryTestIT extends IntegrationBaseTest {
    @Autowired
    private AccidentRepository accidentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldSaveAccident() {
        var accidentToSave = TestEntityBuilder.createAccident();

        var savedAccident = accidentRepository.save(accidentToSave);

        assertNotNull(savedAccident.getId());
    }

    @Test
    void shouldFindByIdAccident() {
        var expectedAccident = Optional.of(ExistEntityBuilder.getExistAccident());

        var actualAccident = accidentRepository.findById(TEST_EXISTS_ACCIDENT_ID);

        assertThat(actualAccident).isNotNull();
        assertEquals(expectedAccident, actualAccident);
    }

    @Test
    void shouldUpdateAccident() {
        var accidentToUpdate = accidentRepository.findById(TEST_EXISTS_ACCIDENT_ID).get();
        var existOrder = orderRepository.findById(TEST_EXISTS_ORDER_ID).get();
        accidentToUpdate.setDamage(BigDecimal.valueOf(3456.76));
        accidentToUpdate.setDescription("test description");
        accidentToUpdate.setOrder(existOrder);

        accidentRepository.update(accidentToUpdate);

        var updatedAccident = accidentRepository.findById(accidentToUpdate.getId()).get();
        assertThat(updatedAccident).isEqualTo(accidentToUpdate);
    }

    @Test
    void shouldDeleteAccident() {
        var accident = accidentRepository.findById(TEST_ACCIDENT_ID_FOR_DELETE);

        accident.ifPresent(acc -> accidentRepository.delete(acc));

        assertThat(accidentRepository.findById(TEST_ACCIDENT_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllAccidents() {
        List<Accident> accidents = accidentRepository.findAll();
        assertThat(accidents).hasSize(2);

        List<BigDecimal> damage = accidents.stream().map(Accident::getDamage).collect(toList());
        assertThat(damage).containsExactlyInAnyOrder(
                BigDecimal.valueOf(75.50).setScale(2), BigDecimal.valueOf(10.05));
    }


    @Test
    void shouldReturnAllAccidentsWithQueryDsl() {
        List<Accident> accidents = accidentRepository.findAllQueryDsl();

        List<String> carNumbers = accidents.stream()
                .map(Accident::getOrder)
                .map(Order::getCar)
                .map(Car::getCarNumber)
                .collect(toList());

        assertThat(accidents).hasSize(2).contains(ExistEntityBuilder.getExistAccident());
        assertThat(carNumbers).containsExactlyInAnyOrder("7865AE-7", "7834AE-7");
    }


    @Test
    void shouldReturnAccidentBYIdWithQueryDsl() {
        var optionalCar = accidentRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_CAR_ID);

        assertThat(optionalCar).isNotNull();
        optionalCar.ifPresent(accident -> assertThat(accident.getId()).isEqualTo(ExistEntityBuilder.getExistAccident().getId()));
        assertThat(optionalCar).isEqualTo(Optional.of(ExistEntityBuilder.getExistAccident()));
    }

    @Test
    void shouldReturnAccidentsByAccidentDateQueryDsl() {
        var accidentsDate = LocalDate.of(2022, 9, 3);

        List<Accident> accidents = accidentRepository.findAccidentsByAccidentDateQueryDsl(accidentsDate);

        assertThat(accidents).hasSize(1).contains(ExistEntityBuilder.getExistAccident());
    }


    @Test
    void shouldReturnAccidentsByCarNumberAndDamageQueryDsl() {
        var accidentFilter = AccidentFilter.builder()
                .carNumber("7834AE-7")
                .damage(BigDecimal.valueOf(10.05))
                .build();

        List<Accident> accidents = accidentRepository.findAccidentsByCarNumberAndDamageQueryDsl(accidentFilter);

        assertThat(accidents).hasSize(1).contains(ExistEntityBuilder.getExistAccident());
    }


    @Test
    void shouldReturnAccidentsByDamageMoreAvgQueryDsl() {
        List<Accident> accidents = accidentRepository.findAccidentsByDamageMoreAvgQueryDsl();

        assertThat(accidents).hasSize(1);
        assertThat(accidents.get(0).getDamage()).isEqualTo(BigDecimal.valueOf(75.50).setScale(2));
    }
}