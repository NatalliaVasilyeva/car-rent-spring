package integration.com.dmdev.repository;

import com.dmdev.domain.dto.AccidentFilter;
import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.Order;
import com.dmdev.repository.AccidentRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

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

    private final Session session = createProxySession(sessionFactory);
    private final AccidentRepository accidentRepository = new AccidentRepository(session);

    @Test
    void shouldSaveAccident() {
        session.beginTransaction();
        var accidentToSave = TestEntityBuilder.createAccident();

        var savedAccident = accidentRepository.save(accidentToSave);

        assertNotNull(savedAccident.getId());
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindByIdAccident() {
        session.beginTransaction();
        var expectedAccident = Optional.of(ExistEntityBuilder.getExistAccident());

        var actualAccident = accidentRepository.findById(TEST_EXISTS_ACCIDENT_ID);

        assertThat(actualAccident).isNotNull();
        assertEquals(expectedAccident, actualAccident);
        session.getTransaction().rollback();
    }

    @Test
    void shouldUpdateAccident() {
        session.beginTransaction();
        var accidentToUpdate = session.find(Accident.class, TEST_EXISTS_ACCIDENT_ID);
        var existOrder = session.find(Order.class, TEST_EXISTS_ORDER_ID);
        accidentToUpdate.setDamage(BigDecimal.valueOf(3456.76));
        accidentToUpdate.setDescription("test description");
        accidentToUpdate.setOrder(existOrder);

        accidentRepository.update(accidentToUpdate);
        session.evict(accidentToUpdate);

        var updatedAccident = session.find(Accident.class, accidentToUpdate.getId());
        assertThat(updatedAccident).isEqualTo(accidentToUpdate);
        session.getTransaction().rollback();
    }

    @Test
    void shouldDeleteAccident() {
        session.beginTransaction();

        accidentRepository.delete(TEST_ACCIDENT_ID_FOR_DELETE);

        assertThat(session.find(Accident.class, TEST_ACCIDENT_ID_FOR_DELETE)).isNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindAllAccidents() {
        session.beginTransaction();

        List<Accident> accidents = accidentRepository.findAll();
        assertThat(accidents).hasSize(2);

        List<BigDecimal> damage = accidents.stream().map(Accident::getDamage).collect(toList());
        assertThat(damage).containsExactlyInAnyOrder(
                BigDecimal.valueOf(75.50).setScale(2), BigDecimal.valueOf(10.05));

        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllAccidentsWithHql() {
        session.beginTransaction();
        List<Accident> accidents = accidentRepository.findAllHql();

        List<String> carNumbers = accidents.stream()
                .map(Accident::getOrder)
                .map(Order::getCar)
                .map(Car::getCarNumber)
                .collect(toList());

        assertThat(accidents).hasSize(2).contains(ExistEntityBuilder.getExistAccident());
        assertThat(carNumbers).containsExactlyInAnyOrder("7865AE-7", "7834AE-7");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllAccidentsWithCriteria() {
        session.beginTransaction();

        List<Accident> accidents = accidentRepository.findAllCriteria();

        List<String> carNumbers = accidents.stream()
                .map(Accident::getOrder)
                .map(Order::getCar)
                .map(Car::getCarNumber)
                .collect(toList());

        assertThat(accidents).hasSize(2).contains(ExistEntityBuilder.getExistAccident());
        assertThat(carNumbers).containsExactlyInAnyOrder("7865AE-7", "7834AE-7");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllAccidentsWithQueryDsl() {
        session.beginTransaction();

        List<Accident> accidents = accidentRepository.findAllQueryDsl();

        List<String> carNumbers = accidents.stream()
                .map(Accident::getOrder)
                .map(Order::getCar)
                .map(Car::getCarNumber)
                .collect(toList());

        assertThat(accidents).hasSize(2).contains(ExistEntityBuilder.getExistAccident());
        assertThat(carNumbers).containsExactlyInAnyOrder("7865AE-7", "7834AE-7");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAccidentBYIdWithCriteria() {
        session.beginTransaction();

        Optional<Accident> optionalCar = accidentRepository.findByIdCriteria(TestEntityIdConst.TEST_EXISTS_ACCIDENT_ID);

        assertThat(optionalCar).isNotNull();
        optionalCar.ifPresent(accident -> assertThat(accident.getId()).isEqualTo(ExistEntityBuilder.getExistAccident().getId()));
        assertThat(optionalCar).isEqualTo(Optional.of(ExistEntityBuilder.getExistAccident()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAccidentBYIdWithQueryDsl() {
        session.beginTransaction();

        Optional<Accident> optionalCar = accidentRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_CAR_ID);

        assertThat(optionalCar).isNotNull();
        optionalCar.ifPresent(accident -> assertThat(accident.getId()).isEqualTo(ExistEntityBuilder.getExistAccident().getId()));
        assertThat(optionalCar).isEqualTo(Optional.of(ExistEntityBuilder.getExistAccident()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAccidentsByAccidentDateCriteria() {
        session.beginTransaction();
        LocalDate accidentsDate = LocalDate.of(2022, 9, 3);

        List<Accident> accidents = accidentRepository.findAccidentsByAccidentDateCriteria(accidentsDate);

        assertThat(accidents).hasSize(1).contains(ExistEntityBuilder.getExistAccident());
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAccidentsByAccidentDateQueryDsl() {
        session.beginTransaction();
        LocalDate accidentsDate = LocalDate.of(2022, 9, 3);

        List<Accident> accidents = accidentRepository.findAccidentsByAccidentDateQueryDsl(accidentsDate);
        session.getTransaction().rollback();

        assertThat(accidents).hasSize(1).contains(ExistEntityBuilder.getExistAccident());
    }

    @Test
    void shouldReturnAccidentsByCarNumberAndDamageCriteria() {
        session.beginTransaction();
        AccidentFilter accidentFilter = AccidentFilter.builder()
                .carNumber("7834AE-7")
                .damage(BigDecimal.valueOf(10.05))
                .build();

        List<Accident> accidents = accidentRepository.findAccidentsByCarNumberAndDamageCriteria(accidentFilter);

        assertThat(accidents).hasSize(1).contains(ExistEntityBuilder.getExistAccident());
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAccidentsByCarNumberAndDamageQueryDsl() {
        session.beginTransaction();
        AccidentFilter accidentFilter = AccidentFilter.builder()
                .carNumber("7834AE-7")
                .damage(BigDecimal.valueOf(10.05))
                .build();

        List<Accident> accidents = accidentRepository.findAccidentsByCarNumberAndDamageQueryDsl(accidentFilter);

        assertThat(accidents).hasSize(1).contains(ExistEntityBuilder.getExistAccident());
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAccidentsByDamageMoreAvgCriteria() {
        session.beginTransaction();

        List<Accident> accidents = accidentRepository.findAccidentsByDamageMoreAvgCriteria();

        assertThat(accidents).hasSize(1);
        assertThat(accidents.get(0).getDamage()).isEqualTo(BigDecimal.valueOf(75.50).setScale(2));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAccidentsByDamageMoreAvgQueryDsl() {
        session.beginTransaction();

        List<Accident> accidents = accidentRepository.findAccidentsByDamageMoreAvgQueryDsl();

        assertThat(accidents).hasSize(1);
        assertThat(accidents.get(0).getDamage()).isEqualTo(BigDecimal.valueOf(75.50).setScale(2));
        session.getTransaction().rollback();
    }
}