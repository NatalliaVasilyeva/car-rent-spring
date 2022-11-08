package integration.com.dmdev.repository;

import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.Order;
import com.dmdev.domain.projection.AccidentFullView;
import com.dmdev.repository.AccidentRepository;
import com.dmdev.repository.OrderRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

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
        var order = orderRepository.findById(TEST_EXISTS_ORDER_ID).get();
        var accidentToSave = TestEntityBuilder.createAccident();
        order.setAccident(accidentToSave);

        var savedAccident = accidentRepository.saveAndFlush(accidentToSave);

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

        accidentRepository.saveAndFlush(accidentToUpdate);

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
    void shouldFindAllAccidentsSortedByOrderAndDamage() {
        Sort sort = Sort.by("order").descending().and(Sort.by("damage")).descending();
        List<Accident> accidents = accidentRepository.findAll(sort);

        assertThat(accidents).hasSize(2);

        List<Long> orderIds = accidents.stream().map(Accident::getOrder).map(Order::getId).collect(toList());
        assertThat(orderIds).containsExactly(
                2L, 1L);

        List<BigDecimal> damage = accidents.stream().map(Accident::getDamage).collect(toList());
        assertThat(damage).containsExactly(
                BigDecimal.valueOf(10.05), BigDecimal.valueOf(75.50).setScale(2));
    }

    @Test
    void shouldFindAllAccidentsByOrderId() {
        List<Accident> accidents = accidentRepository.findAllByOrderId(TEST_EXISTS_ORDER_ID);

        assertThat(accidents).hasSize(1);
        assertThat(accidents.get(0)).isEqualTo(ExistEntityBuilder.getExistAccident());
    }

    @Test
    void shouldFindAllAccidentsByNameAndSurname() {
        List<Accident> accidents = accidentRepository.findAllByNameAndSurname("Petia", "Petrov");

        assertThat(accidents).hasSize(1);
        assertThat(accidents.get(0)).isEqualTo(ExistEntityBuilder.getExistAccident());
    }

    @Test
    void shouldFindAllAccidentsByCarNumber() {
        List<Accident> accidents = accidentRepository.findAllByCarNumber("AE");
        assertThat(accidents).hasSize(2);
        List<String> carNumbers = accidents.stream()
                .map(Accident::getOrder)
                .map(Order::getCar)
                .map(Car::getCarNumber)
                .collect(toList());

        assertThat(accidents).hasSize(2);
        assertThat(carNumbers).containsExactlyInAnyOrder("7865AE-7", "7834AE-7");
    }

    @Test
    void shouldReturnAccidentsByAccidentDate() {
        var accidentsDate = LocalDate.of(2022, 9, 3);

        List<Accident> accidents = accidentRepository.findAllByAccidentDateOrderByAccidentDateDesc(accidentsDate);

        assertThat(accidents).hasSize(1).contains(ExistEntityBuilder.getExistAccident());
    }

    @Test
    void shouldReturnAccidentsByAccidentBetweenDates() {
        var start = LocalDate.of(2022, 8, 3);
        var end = LocalDate.of(2022, 9, 3);

        List<Accident> accidents = accidentRepository.findAllByAccidentDateBetween(start, end);

        assertThat(accidents).hasSize(1).contains(ExistEntityBuilder.getExistAccident());
    }

    @Test
    void shouldReturnAccidentsByMoreAvgDamage() {
        List<Accident> accidents = accidentRepository.findAllByAvgDamageMore();

        assertThat(accidents).hasSize(1);
        assertThat(accidents.get(0).getDamage()).isEqualTo(BigDecimal.valueOf(75.50).setScale(2));
    }

    @Test
    void shouldReturnAccidentsByMoreDamage() {
        List<Accident> accidents = accidentRepository.findAllByDamage(BigDecimal.valueOf(75.50).setScale(2));

        assertThat(accidents).hasSize(1);
        assertThat(accidents.get(0).getDamage()).isEqualTo(BigDecimal.valueOf(75.50).setScale(2));
    }

    @Test
    void shouldSelectAllFullAccidentView() {
        List<AccidentFullView> accidents = accidentRepository.findAllFull();
        assertThat(accidents).hasSize(2);

        List<String> carNumbers = accidents.stream()
                .map(AccidentFullView::getCarNumber)
                .collect(toList());

        List<String> brands = accidents.stream()
                .map(AccidentFullView::getBrandName)
                .collect(toList());

        assertThat(carNumbers).containsExactlyInAnyOrder("7865AE-7", "7834AE-7");
        assertThat(brands).containsExactlyInAnyOrder("audi", "mercedes");
    }

    @Test
    void shouldReturnFullAccidentsViewById() {
        List<AccidentFullView> accidents = accidentRepository.findByIdFull(TEST_EXISTS_ACCIDENT_ID);

        assertThat(accidents).hasSize(1);
        assertThat(accidents.get(0).getDamage()).isEqualTo(BigDecimal.valueOf(10.05).setScale(2));
        assertThat(accidents.get(0).getBrandName()).isEqualTo("mercedes");
        assertThat(accidents.get(0).getModelName()).isEqualTo("Benz");
    }
}