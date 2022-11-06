package integration.com.dmdev.repository;

import com.dmdev.domain.dto.DriverLicenseFilter;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.repository.DriverLicenseRepository;
import com.dmdev.repository.UserDetailsRepository;
import com.querydsl.core.Tuple;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Autowired
    private DriverLicenseRepository driverLicenseRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Test
    void shouldSaveDriverLicense() {
        var userDetails = userDetailsRepository.findById(TEST_EXISTS_USER_DETAILS_ID).get();
        var driverLicenceToSave = TestEntityBuilder.createDriverLicense();
        userDetails.setDriverLicense(driverLicenceToSave);

        var savedDriverLicense = driverLicenseRepository.save(driverLicenceToSave);

        assertThat(savedDriverLicense).isNotNull();
    }

    @Test
    void shouldFindByIdDriverLicense() {
        var expectedDriverLicense = Optional.of(ExistEntityBuilder.getExistDriverLicense());

        var actualDriverLicense = driverLicenseRepository.findById(TEST_EXISTS_DRIVER_LICENSE_ID);

        assertThat(actualDriverLicense).isNotNull();
        assertEquals(expectedDriverLicense, actualDriverLicense);
    }

    @Test
    void shouldUpdateDriverLicense() {
        var driverLicenseToUpdate = driverLicenseRepository.findById(TEST_EXISTS_DRIVER_LICENSE_ID).get();
        driverLicenseToUpdate.setNumber("dn36632");

        driverLicenseRepository.update(driverLicenseToUpdate);

        var updatedDriverLicense = driverLicenseRepository.findById(driverLicenseToUpdate.getId()).get();

        assertThat(updatedDriverLicense).isEqualTo(driverLicenseToUpdate);
    }

    @Test
    void shouldDeleteDriverLicense() {
        var driverLicense = driverLicenseRepository.findById(TEST_DRIVER_LICENSE_ID_FOR_DELETE);
        driverLicense.ifPresent(dl -> driverLicenseRepository.delete(dl));

        assertThat(driverLicenseRepository.findById(TEST_DRIVER_LICENSE_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllDriverLicences() {
        List<DriverLicense> driverLicenses = driverLicenseRepository.findAll();
        assertThat(driverLicenses).hasSize(2);

        List<String> numbers = driverLicenses.stream().map(DriverLicense::getNumber).collect(toList());
        assertThat(numbers).containsExactlyInAnyOrder("AB12345", "AB12346");
    }

    @Test
    void shouldReturnAllDriverLicensesWithQueryDsl() {
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
    }

    @Test
    void shouldReturnDriverLicenseBIdWithQueryDsl() {
        var optionalDriverLicense = driverLicenseRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_DRIVER_LICENSE_ID);

        assertThat(optionalDriverLicense).isNotNull();
        optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense.getId()).isEqualTo(ExistEntityBuilder.getExistDriverLicense().getId()));
        assertThat(optionalDriverLicense).isEqualTo(Optional.of(ExistEntityBuilder.getExistDriverLicense()));
    }

    @Test
    void shouldReturnDriverLicenseByNumberCriteria() {

        var optionalDriverLicense = driverLicenseRepository.findDriverLicenseByNumberCriteria("AB12346");

        assertThat(optionalDriverLicense).isNotNull();
        optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense.getId()).isEqualTo(ExistEntityBuilder.getExistDriverLicense().getId()));
        assertThat(optionalDriverLicense).isEqualTo(Optional.of(ExistEntityBuilder.getExistDriverLicense()));
    }

    @Test
    void shouldNotReturnDriverLicenseByExpiredDateOrLessCriteria() {

        var driverLicenseFilter = DriverLicenseFilter.builder()
                .expiredDate(LocalDate.now().minusDays(1L))
                .build();

        List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicenseByExpiredDateOrLessCriteria(driverLicenseFilter);

        assertThat(driverLicenses).isEmpty();
    }

    @Test
    void shouldReturnDriverLicensesByIssueAndExpiredDateQueryDsl() {
        var driverLicenseFilter = DriverLicenseFilter.builder()
                .issueDate(LocalDate.of(2000, 1, 1))
                .expiredDate(LocalDate.of(2030, 1, 1))
                .build();

        List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicensesByIssueAndExpiredDateQueryDsl(driverLicenseFilter);

        assertThat(driverLicenses).hasSize(2).contains(ExistEntityBuilder.getExistDriverLicense());
    }

    @Test
    void shouldReturnDriverLicenseTupleByExpiredDateOrderBySurnameQueryDsl() {
        var driverLicenseFilter = DriverLicenseFilter.builder()
                .expiredDate(LocalDate.of(2025, 1, 1))
                .build();

        List<Tuple> driverLicenses = driverLicenseRepository.findDriverLicensesTupleByExpiredDateOrderBySurnameQueryDsl(driverLicenseFilter);

        assertThat(driverLicenses).hasSize(1);
        List<String> surname = driverLicenses.stream().map(r -> r.get(1, String.class)).collect(toList());
        assertThat(surname).contains("Petrov");

        List<String> driverLicenseNumber = driverLicenses.stream().map(r -> r.get(3, String.class)).collect(toList());
        assertThat(driverLicenseNumber).contains("AB12346");
    }
}