package integration.com.dmdev.repository;

import com.dmdev.domain.dto.DriverLicenseDto;
import com.dmdev.domain.dto.DriverLicenseFilter;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.repository.DriverLicenseRepository;
import com.querydsl.core.Tuple;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class DriverLicenseRepositoryTestIT extends IntegrationBaseTest {

    private final DriverLicenseRepository driverLicenseRepository = DriverLicenseRepository.getInstance();

    @Test
    void shouldReturnAllDriverLicensesWithHql() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findAllHql(session);

            assertThat(driverLicenses).hasSize(2);

            List<String> driverLicenseNumbers = driverLicenses.stream().map(DriverLicense::getNumber).collect(toList());
            assertThat(driverLicenseNumbers).contains("AB12345", "AB12346");

            List<String> userEmails = driverLicenses.stream()
                    .map(DriverLicense::getUserDetails)
                    .map(UserDetails::getUser)
                    .map(User::getEmail)
                    .collect(toList());

            assertThat(userEmails).contains("admin@gmail.com", "client@gmail.com");
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnAllDriverLicensesWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findAllCriteria(session);

            assertThat(driverLicenses).hasSize(2);

            List<String> driverLicenseNumbers = driverLicenses.stream().map(DriverLicense::getNumber).collect(toList());
            assertThat(driverLicenseNumbers).contains("AB12345", "AB12346");

            List<String> userEmails = driverLicenses.stream()
                    .map(DriverLicense::getUserDetails)
                    .map(UserDetails::getUser)
                    .map(User::getEmail)
                    .collect(toList());

            assertThat(userEmails).contains("admin@gmail.com", "client@gmail.com");
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnAllDriverLicensesWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findAllQueryDsl(session);

            assertThat(driverLicenses).hasSize(2);

            List<String> driverLicenseNumbers = driverLicenses.stream().map(DriverLicense::getNumber).collect(toList());
            assertThat(driverLicenseNumbers).contains("AB12345", "AB12346");

            List<String> userEmails = driverLicenses.stream()
                    .map(DriverLicense::getUserDetails)
                    .map(UserDetails::getUser)
                    .map(User::getEmail)
                    .collect(toList());

            assertThat(userEmails).contains("admin@gmail.com", "client@gmail.com");
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnDriverLicenseByIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<DriverLicense> optionalDriverLicense = driverLicenseRepository.findByIdCriteria(session, TestEntityIdConst.TEST_EXISTS_DRIVER_LICENSE_ID);

            assertThat(optionalDriverLicense).isNotNull();
            optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense.getId()).isEqualTo(ExistEntityBuilder.getExistDriverLicense().getId()));
            assertThat(optionalDriverLicense).isEqualTo(Optional.of(ExistEntityBuilder.getExistDriverLicense()));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnDriverLicenseBIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<DriverLicense> optionalDriverLicense = driverLicenseRepository.findByIdQueryDsl(session, TestEntityIdConst.TEST_EXISTS_DRIVER_LICENSE_ID);

            assertThat(optionalDriverLicense).isNotNull();
            optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense.getId()).isEqualTo(ExistEntityBuilder.getExistDriverLicense().getId()));
            assertThat(optionalDriverLicense).isEqualTo(Optional.of(ExistEntityBuilder.getExistDriverLicense()));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnDriverLicenseByNumberCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<DriverLicense> optionalDriverLicense = driverLicenseRepository.findDriverLicenseByNumberCriteria(session, "AB12346");

            assertThat(optionalDriverLicense).isNotNull();
            optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense.getId()).isEqualTo(ExistEntityBuilder.getExistDriverLicense().getId()));
            assertThat(optionalDriverLicense).isEqualTo(Optional.of(ExistEntityBuilder.getExistDriverLicense()));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldNotReturnDriverLicenseByExpiredDateOrLessCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                    .expiredDate(LocalDate.now().minusDays(1L))
                    .build();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicenseByExpiredDateOrLessCriteria(session, driverLicenseFilter);

            assertThat(driverLicenses).isEmpty();
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnDriverLicensesByIssueAndExpiredDateCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                    .issueDate(LocalDate.of(2000, 1, 1))
                    .expiredDate(LocalDate.of(2030, 1, 1))
                    .build();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicensesByIssueAndExpiredDateCriteria(session, driverLicenseFilter);

            assertThat(driverLicenses).hasSize(2);
            assertThat(driverLicenses).contains(ExistEntityBuilder.getExistDriverLicense());
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnDriverLicensesByIssueAndExpiredDateQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                    .issueDate(LocalDate.of(2000, 1, 1))
                    .expiredDate(LocalDate.of(2030, 1, 1))
                    .build();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicensesByIssueAndExpiredDateQueryDsl(session, driverLicenseFilter);

            assertThat(driverLicenses).hasSize(2);
            assertThat(driverLicenses).contains(ExistEntityBuilder.getExistDriverLicense());
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnDriverLicenseDtoByExpiredDateOrderBySurnameCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                    .expiredDate(LocalDate.of(2030, 1, 1))
                    .build();
            List<DriverLicenseDto> driverLicenseDtos = driverLicenseRepository.findDriverLicensesByExpiredDateOrderBySurnameCriteria(session, driverLicenseFilter);

            assertThat(driverLicenseDtos).hasSize(2);
            assertThat(driverLicenseDtos.get(0).getNumber()).isEqualTo("AB12345");
            assertThat(driverLicenseDtos.get(1).getNumber()).isEqualTo("AB12346");
            assertThat(driverLicenseDtos.get(0).getSurname()).isEqualTo("Ivanov");
            assertThat(driverLicenseDtos.get(1).getSurname()).isEqualTo("Petrov");
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnDriverLicenseTupleByExpiredDateOrderBySurnameQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                    .expiredDate(LocalDate.of(2025, 1, 1))
                    .build();
            List<Tuple> driverLicenses = driverLicenseRepository.findDriverLicensesTupleByExpiredDateOrderBySurnameQueryDsl(session, driverLicenseFilter);

            assertThat(driverLicenses).hasSize(1);
            List<String> surname = driverLicenses.stream().map(r -> r.get(1, String.class)).collect(toList());
            assertThat(surname).contains("Petrov");

            List<String> driverLicenseNumber = driverLicenses.stream().map(r -> r.get(3, String.class)).collect(toList());
            assertThat(driverLicenseNumber).contains("AB12346");
            session.getTransaction().rollback();
        }
    }
}