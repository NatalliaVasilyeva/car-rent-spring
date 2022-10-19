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
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_DRIVER_LICENSE_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_DRIVER_LICENSE_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_USER_DETAILS_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DriverLicenseRepositoryTestIT extends IntegrationBaseTest {

    private final Session session = createProxySession(sessionFactory);
    private final DriverLicenseRepository driverLicenseRepository = new DriverLicenseRepository(session);

    @Test
    void shouldSaveDriverLicense() {
        session.beginTransaction();
        var userDetails = session.find(UserDetails.class, TEST_EXISTS_USER_DETAILS_ID);
        var driverLicenceToSave = TestEntityBuilder.createDriverLicense();
        userDetails.setDriverLicense(driverLicenceToSave);

        var savedDriverLicense = driverLicenseRepository.save(driverLicenceToSave);

        assertThat(savedDriverLicense).isNotNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindByIdDriverLicense() {
        session.beginTransaction();
        var expectedDriverLicense = Optional.of(ExistEntityBuilder.getExistDriverLicense());

        var actualDriverLicense = driverLicenseRepository.findById(TEST_EXISTS_DRIVER_LICENSE_ID);

        assertThat(actualDriverLicense).isNotNull();
        assertEquals(expectedDriverLicense, actualDriverLicense);
        session.getTransaction().rollback();
    }

    @Test
    void shouldUpdateDriverLicense() {
        session.beginTransaction();
        var driverLicenseToUpdate = session.find(DriverLicense.class, TEST_EXISTS_DRIVER_LICENSE_ID);
        driverLicenseToUpdate.setNumber("dn36632");

        driverLicenseRepository.update(driverLicenseToUpdate);
        session.evict(driverLicenseToUpdate);

        var updatedDriverLicense = session.find(DriverLicense.class, driverLicenseToUpdate.getId());

        assertThat(updatedDriverLicense).isEqualTo(driverLicenseToUpdate);
        session.getTransaction().rollback();
    }

    @Test
    void shouldDeleteDriverLicense() {
        session.beginTransaction();

        driverLicenseRepository.delete(TEST_DRIVER_LICENSE_ID_FOR_DELETE);

        assertThat(session.find(DriverLicense.class, TEST_DRIVER_LICENSE_ID_FOR_DELETE)).isNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindAllDriverLicences() {
        session.beginTransaction();

        List<DriverLicense> driverLicenses = driverLicenseRepository.findAll();
        assertThat(driverLicenses).hasSize(2);

        List<String> numbers = driverLicenses.stream().map(DriverLicense::getNumber).collect(toList());
        assertThat(numbers).containsExactlyInAnyOrder("AB12345", "AB12346");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllDriverLicensesWithHql() {
        session.beginTransaction();

        List<DriverLicense> driverLicenses = driverLicenseRepository.findAllHql();

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

    @Test
    void shouldReturnAllDriverLicensesWithCriteria() {
        session.beginTransaction();

        List<DriverLicense> driverLicenses = driverLicenseRepository.findAllCriteria();

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

    @Test
    void shouldReturnAllDriverLicensesWithQueryDsl() {
        session.beginTransaction();

        List<DriverLicense> driverLicenses = driverLicenseRepository.findAllQueryDsl();

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

    @Test
    void shouldReturnDriverLicenseByIdWithCriteria() {
        session.beginTransaction();

        Optional<DriverLicense> optionalDriverLicense = driverLicenseRepository.findByIdCriteria(TestEntityIdConst.TEST_EXISTS_DRIVER_LICENSE_ID);

        assertThat(optionalDriverLicense).isNotNull();
        optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense.getId()).isEqualTo(ExistEntityBuilder.getExistDriverLicense().getId()));
        assertThat(optionalDriverLicense).isEqualTo(Optional.of(ExistEntityBuilder.getExistDriverLicense()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnDriverLicenseBIdWithQueryDsl() {
        session.beginTransaction();

        Optional<DriverLicense> optionalDriverLicense = driverLicenseRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_DRIVER_LICENSE_ID);

        assertThat(optionalDriverLicense).isNotNull();
        optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense.getId()).isEqualTo(ExistEntityBuilder.getExistDriverLicense().getId()));
        assertThat(optionalDriverLicense).isEqualTo(Optional.of(ExistEntityBuilder.getExistDriverLicense()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnDriverLicenseByNumberCriteria() {
        session.beginTransaction();

        Optional<DriverLicense> optionalDriverLicense = driverLicenseRepository.findDriverLicenseByNumberCriteria("AB12346");

        assertThat(optionalDriverLicense).isNotNull();
        optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense.getId()).isEqualTo(ExistEntityBuilder.getExistDriverLicense().getId()));
        assertThat(optionalDriverLicense).isEqualTo(Optional.of(ExistEntityBuilder.getExistDriverLicense()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldNotReturnDriverLicenseByExpiredDateOrLessCriteria() {
        session.beginTransaction();
        DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                .expiredDate(LocalDate.now().minusDays(1L))
                .build();

        List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicenseByExpiredDateOrLessCriteria(driverLicenseFilter);

        assertThat(driverLicenses).isEmpty();
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnDriverLicensesByIssueAndExpiredDateCriteria() {
        session.beginTransaction();
        DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                .issueDate(LocalDate.of(2000, 1, 1))
                .expiredDate(LocalDate.of(2030, 1, 1))
                .build();

        List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicensesByIssueAndExpiredDateCriteria(driverLicenseFilter);

        assertThat(driverLicenses).hasSize(2).contains(ExistEntityBuilder.getExistDriverLicense());
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnDriverLicensesByIssueAndExpiredDateQueryDsl() {
        session.beginTransaction();
        DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                .issueDate(LocalDate.of(2000, 1, 1))
                .expiredDate(LocalDate.of(2030, 1, 1))
                .build();

        List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicensesByIssueAndExpiredDateQueryDsl(driverLicenseFilter);

        assertThat(driverLicenses).hasSize(2).contains(ExistEntityBuilder.getExistDriverLicense());
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnDriverLicenseDtoByExpiredDateOrderBySurnameCriteria() {
        session.beginTransaction();
        DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                .expiredDate(LocalDate.of(2030, 1, 1))
                .build();

        List<DriverLicenseDto> driverLicenseDtos = driverLicenseRepository.findDriverLicensesByExpiredDateOrderBySurnameCriteria(driverLicenseFilter);

        assertThat(driverLicenseDtos).hasSize(2);
        assertThat(driverLicenseDtos.get(0).getNumber()).isEqualTo("AB12345");
        assertThat(driverLicenseDtos.get(1).getNumber()).isEqualTo("AB12346");
        assertThat(driverLicenseDtos.get(0).getSurname()).isEqualTo("Ivanov");
        assertThat(driverLicenseDtos.get(1).getSurname()).isEqualTo("Petrov");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnDriverLicenseTupleByExpiredDateOrderBySurnameQueryDsl() {
        session.beginTransaction();
        DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                .expiredDate(LocalDate.of(2025, 1, 1))
                .build();

        List<Tuple> driverLicenses = driverLicenseRepository.findDriverLicensesTupleByExpiredDateOrderBySurnameQueryDsl(driverLicenseFilter);

        assertThat(driverLicenses).hasSize(1);
        List<String> surname = driverLicenses.stream().map(r -> r.get(1, String.class)).collect(toList());
        assertThat(surname).contains("Petrov");

        List<String> driverLicenseNumber = driverLicenses.stream().map(r -> r.get(3, String.class)).collect(toList());
        assertThat(driverLicenseNumber).contains("AB12346");
        session.getTransaction().rollback();
    }
}