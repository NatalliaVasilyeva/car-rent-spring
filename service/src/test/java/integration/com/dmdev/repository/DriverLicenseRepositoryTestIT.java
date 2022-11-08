package integration.com.dmdev.repository;

import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.projection.DriverLicenseFullView;
import com.dmdev.repository.DriverLicenseRepository;
import com.dmdev.repository.UserDetailsRepository;
import integration.com.dmdev.IntegrationBaseTest;
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
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_USER_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        driverLicenseRepository.save(driverLicenseToUpdate);

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
    void shouldReturnDriverLicenseByNumber() {
        var optionalDriverLicense = driverLicenseRepository.findByNumberContainingIgnoreCase("Ab12346");

        assertThat(optionalDriverLicense).isNotNull();
        optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense.getId()).isEqualTo(ExistEntityBuilder.getExistDriverLicense().getId()));
        assertThat(optionalDriverLicense).isEqualTo(Optional.of(ExistEntityBuilder.getExistDriverLicense()));
    }

    @Test
    void shouldNotReturnDriverLicenseByExpiredDateOrLess() {
        List<DriverLicense> driverLicenses = driverLicenseRepository.findByExpiredDateLessThanEqual(LocalDate.now().minusDays(1L));

        assertThat(driverLicenses).isEmpty();
    }

    @Test
    void shouldReturnDriverLicensesByIssueAndExpiredDateOrderBySurname() {
        List<DriverLicenseFullView> driverLicenses = driverLicenseRepository.findDriverLicensesFullViewByIssueAndExpiredDate(LocalDate.of(2000, 1, 1), LocalDate.of(2030, 1, 1));

        assertThat(driverLicenses).hasSize(2);

        List<String> phones = driverLicenses.stream()
                .map(DriverLicenseFullView::getPhone)
                .collect(toList());

        assertThat(phones).containsExactly(
                "+375 29 124 56 78", "+375 29 124 56 79");
    }

    @Test
    void shouldReturnDriverLicenseByUserId() {
        Optional<DriverLicense> driverLicense = driverLicenseRepository.findByUserId(TEST_EXISTS_USER_ID);

        assertTrue(driverLicense.isPresent());

        driverLicense.ifPresent(
                dl -> assertEquals(dl, ExistEntityBuilder.getExistDriverLicense())
        );
    }
}