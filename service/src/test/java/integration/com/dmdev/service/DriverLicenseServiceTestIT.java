package integration.com.dmdev.service;

import com.dmdev.domain.dto.driverlicense.request.DriverLicenseUpdateRequestDto;
import com.dmdev.domain.dto.driverlicense.response.DriverLicenseResponseDto;
import com.dmdev.service.DriverLicenseService;
import com.dmdev.service.UserService;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.TestDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_DRIVER_LICENSE_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class DriverLicenseServiceTestIT extends IntegrationBaseTest {

    private final DriverLicenseService driverLicenseService;
    private final UserService userService;

    @Test
    void shouldSaveDriverLicenseCorrectly() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();

        var actualUser = userService.create(userCreateRequestDTO);

        assertTrue(actualUser.isPresent());
        assertEquals(userCreateRequestDTO.getDriverLicenseNumber(), actualUser.get().getDriverLicenseDto().getDriverLicenseNumber());
        assertEquals(userCreateRequestDTO.getDriverLicenseIssueDate(), actualUser.get().getDriverLicenseDto().getDriverLicenseIssueDate());
        assertEquals(userCreateRequestDTO.getDriverLicenseExpiredDate(), actualUser.get().getDriverLicenseDto().getDriverLicenseExpiredDate());
    }

    @Test
    void shouldFindAllDriverLicenses() {
        var driverLicenses = driverLicenseService.getAll(0, 4);

        assertThat(driverLicenses).hasSize(2);
        assertThat(driverLicenses.getTotalElements()).isEqualTo(2L);
        assertThat(driverLicenses.getNumberOfElements()).isEqualTo(2L);

       var addresses = driverLicenses.getContent().stream().map(DriverLicenseResponseDto::getDriverLicenseNumber).collect(toList());
        assertThat(addresses).containsExactlyInAnyOrder("AB12345", "AB12346");
    }

    @Test
    void shouldFindAllExpiredDriverLicenses() {
        var driverLicenses = driverLicenseService.getAllExpiredDriverLicenses();

        assertThat(driverLicenses).hasSize(0);
    }


    @Test
    void shouldReturnDriverLicensesByNumber() {
        var userCreateRequestDto = TestDtoBuilder.createUserCreateRequestDTO();
        userService.create(userCreateRequestDto);

        var userDetails = driverLicenseService.getByNumber("ae");

        assertThat(userDetails).isEmpty();
    }

    @Test
    void shouldReturnDriverLicenseById() {
        var userCreateRequestDto = TestDtoBuilder.createUserCreateRequestDTO();
        var expectedUserDetails = userService.create(userCreateRequestDto).get().getDriverLicenseDto();

        var actualDriverLicense = driverLicenseService.getById(expectedUserDetails.getId());

        assertThat(actualDriverLicense).isNotNull();
        assertEquals(expectedUserDetails, actualDriverLicense.get());
    }

    @Test
    void shouldUpdateDriverLicenseCorrectly() {
        var userCreateRequestDto = TestDtoBuilder.createUserCreateRequestDTO();
        var driverLicenseUpdateRequestDto = new DriverLicenseUpdateRequestDto(
                "number_test",
                LocalDate.now(),
                LocalDate.now().plusYears(1));
        var savedDriverLicense = userService.create(userCreateRequestDto).get().getDriverLicenseDto();

        var actualDriverLicense = driverLicenseService.update(savedDriverLicense.getId(), driverLicenseUpdateRequestDto);

        assertThat(actualDriverLicense).isNotNull();
        actualDriverLicense.ifPresent(userDetail -> {
            assertEquals(driverLicenseUpdateRequestDto.getDriverLicenseNumber(), userDetail.getDriverLicenseNumber());
            assertEquals(driverLicenseUpdateRequestDto.getDriverLicenseIssueDate(), userDetail.getDriverLicenseIssueDate());
            assertSame(driverLicenseUpdateRequestDto.getDriverLicenseExpiredDate(), userDetail.getDriverLicenseExpiredDate());
        });
    }

    @Test
    void shouldDeleteUserDetailByIdCorrectly() {
        assertTrue(driverLicenseService.deleteById(TEST_DRIVER_LICENSE_ID_FOR_DELETE));
    }

    @Test
    void shouldNotDeleteUserWithNonExistsId() {
        assertFalse(driverLicenseService.deleteById(999999L));
    }
}