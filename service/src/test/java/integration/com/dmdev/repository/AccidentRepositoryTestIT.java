package integration.com.dmdev.repository;

import com.dmdev.domain.dto.AccidentFilter;
import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.Order;
import com.dmdev.repository.AccidentRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class AccidentRepositoryTestIT extends IntegrationBaseTest {

    private final AccidentRepository accidentRepository = AccidentRepository.getInstance();

    @Test
    void shouldReturnAllAccidentsWithHql() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Accident> accidents = accidentRepository.findAllHQL(session);
            session.getTransaction().commit();

            List<String> carNumbers = accidents.stream()
                    .map(Accident::getOrder)
                    .map(Order::getCar)
                    .map(Car::getCarNumber)
                    .collect(toList());

            assertThat(accidents).hasSize(2);
            assertThat(accidents).contains(ExistEntityBuilder.getExistAccident());
            assertThat(carNumbers).containsExactlyInAnyOrder("7865AE-7", "7834AE-7");
        }
    }

    @Test
    void shouldReturnAllAccidentsWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Accident> accidents = accidentRepository.findAllCriteria(session);
            session.getTransaction().commit();

            List<String> carNumbers = accidents.stream()
                    .map(Accident::getOrder)
                    .map(Order::getCar)
                    .map(Car::getCarNumber)
                    .collect(toList());

            assertThat(accidents).hasSize(2);
            assertThat(accidents).contains(ExistEntityBuilder.getExistAccident());
            assertThat(carNumbers).containsExactlyInAnyOrder("7865AE-7", "7834AE-7");
        }
    }

    @Test
    void shouldReturnAllAccidentsWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Accident> accidents = accidentRepository.findAllQueryDsl(session);
            session.getTransaction().commit();

            List<String> carNumbers = accidents.stream()
                    .map(Accident::getOrder)
                    .map(Order::getCar)
                    .map(Car::getCarNumber)
                    .collect(toList());

            assertThat(accidents).hasSize(2);
            assertThat(accidents).contains(ExistEntityBuilder.getExistAccident());
            assertThat(carNumbers).containsExactlyInAnyOrder("7865AE-7", "7834AE-7");
        }
    }

    @Test
    void shouldReturnAccidentBYIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Accident> optionalCar = accidentRepository.findByIdCriteria(session, TestEntityIdConst.TEST_EXISTS_ACCIDENT_ID);
            session.getTransaction().commit();

            assertThat(optionalCar).isNotNull();
            optionalCar.ifPresent(accident -> assertThat(accident).isEqualTo(ExistEntityBuilder.getExistAccident()));

        }
    }

    @Test
    void shouldReturnAccidentBYIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Accident> optionalCar = accidentRepository.findByIdQueryDsl(session, TestEntityIdConst.TEST_EXISTS_CAR_ID);
            session.getTransaction().commit();

            assertThat(optionalCar).isNotNull();
            optionalCar.ifPresent(accident -> assertThat(accident).isEqualTo(ExistEntityBuilder.getExistAccident()));

        }
    }

    @Test
    void shouldReturnAccidentsByAccidentDateCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            LocalDate accidentsDate = LocalDate.of(2022, 9, 3);
            List<Accident> accidents = accidentRepository.findAccidentsByAccidentDateCriteria(session, accidentsDate);
            session.getTransaction().commit();

            assertThat(accidents).hasSize(1);
            assertThat(accidents).contains(ExistEntityBuilder.getExistAccident());
        }
    }

    @Test
    void shouldReturnAccidentsByAccidentDateQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            LocalDate accidentsDate = LocalDate.of(2022, 9, 3);
            List<Accident> accidents = accidentRepository.findAccidentsByAccidentDateQueryDsl(session, accidentsDate);
            session.getTransaction().commit();

            assertThat(accidents).hasSize(1);
            assertThat(accidents).contains(ExistEntityBuilder.getExistAccident());
        }
    }

    @Test
    void shouldReturnAccidentsByCarNumberAndDamageCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            AccidentFilter accidentFilter = AccidentFilter.builder()
                    .carNumber("7834AE-7")
                    .damage(BigDecimal.valueOf(10.05))
                    .build();

            List<Accident> accidents = accidentRepository.findAccidentsByCarNumberAndDamageCriteria(session, accidentFilter);
            session.getTransaction().commit();

            assertThat(accidents).hasSize(1);
            assertThat(accidents).contains(ExistEntityBuilder.getExistAccident());
        }
    }

    @Test
    void shouldReturnAccidentsByCarNumberAndDamageQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            AccidentFilter accidentFilter = AccidentFilter.builder()
                    .carNumber("7834AE-7")
                    .damage(BigDecimal.valueOf(10.05))
                    .build();

            List<Accident> accidents = accidentRepository.findAccidentsByCarNumberAndDamageQueryDsl(session, accidentFilter);
            session.getTransaction().commit();

            assertThat(accidents).hasSize(1);
            assertThat(accidents).contains(ExistEntityBuilder.getExistAccident());
        }
    }

    @Test
    void shouldReturnAccidentsByDamageMoreAvgCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Accident> accidents = accidentRepository.findAccidentsByDamageMoreAvgCriteria(session);
            session.getTransaction().commit();

            assertThat(accidents).hasSize(1);
            assertThat(accidents.get(0).getDamage()).isEqualTo(BigDecimal.valueOf(75.50).setScale(2));
        }
    }

    @Test
    void shouldReturnAccidentsByDamageMoreAvgQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Accident> accidents = accidentRepository.findAccidentsByDamageMoreAvgQueryDsl(session);
            session.getTransaction().commit();

            assertThat(accidents).hasSize(1);
            assertThat(accidents.get(0).getDamage()).isEqualTo(BigDecimal.valueOf(75.50).setScale(2));
        }
    }
}