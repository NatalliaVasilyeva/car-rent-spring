package integration.com.dmdev.utils.builder;

import com.dmdev.domain.dto.driverlicense.response.DriverLicenseResponseDto;
import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.dto.user.request.UserUpdateRequestDto;
import com.dmdev.domain.dto.user.response.UserResponseDto;
import com.dmdev.domain.dto.userdetails.response.UserDetailsResponseDto;
import com.dmdev.domain.model.Role;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class TestDtoBuilder {


    public static UserCreateRequestDto createUserCreateRequestDTO() {
        return new UserCreateRequestDto(
                "test@gmal.com",
                "test",
                "test",
                "petia",
                "petrov",
                "Minsk",
                "+37529111111111",
                LocalDate.of(2000, 10, 10),
                "AG67482",
                LocalDate.of(2020, 10, 10),
                LocalDate.of(2030, 10, 10));
    }

    public static UserCreateRequestDto createUserCreateRequestDTOWithExistsEmail() {
        return new UserCreateRequestDto(
                "admin@gmail.com",
                "test",
                "test",
                "petia",
                "petrov",
                "Minsk",
                "+37529111111111",
                LocalDate.of(2000, 10, 10),
                "AG67482",
                LocalDate.of(2020, 10, 10),
                LocalDate.of(2030, 10, 10));
    }


    public static UserResponseDto getUserResponseDto() {
        return UserResponseDto.builder()
                .email("test@gmal.com")
                .login("test")
                .userDetailsDto(UserDetailsResponseDto.builder()
                        .name("petia")
                        .surname("petrov")
                        .address("Minsk")
                        .phone("+37529111111111")
                        .birthday(LocalDate.of(2000, 10, 10))
                        .build())
                .driverLicenseDto(DriverLicenseResponseDto.builder()
                        .driverLicenseNumber("AG67482")
                        .driverLicenseIssueDate(LocalDate.of(2020, 10, 10))
                        .driverLicenseExpiredDate(LocalDate.of(2030, 10, 10))
                        .build())
                .build();
    }

    public static UserUpdateRequestDto createUserUpdateRequestDTO() {
        return new UserUpdateRequestDto(
                "test1@gmal.com",
                "test",
                Role.CLIENT);
    }
}