package integration.com.dmdev.utils.builder;

import com.dmdev.domain.dto.brand.request.BrandCreateEditRequestDto;
import com.dmdev.domain.dto.car.CarCreateRequestDto;
import com.dmdev.domain.dto.car.CarUpdateRequestDto;
import com.dmdev.domain.dto.carrentaltime.CarRentalTimeCreateRequestDto;
import com.dmdev.domain.dto.carrentaltime.CarRentalTimeUpdateRequestDto;
import com.dmdev.domain.dto.category.request.CategoryCreateEditRequestDto;
import com.dmdev.domain.dto.driverlicense.request.DriverLicenseUpdateRequestDto;
import com.dmdev.domain.dto.driverlicense.response.DriverLicenseResponseDto;
import com.dmdev.domain.dto.model.ModelCreateRequestDto;
import com.dmdev.domain.dto.model.ModelUpdateRequestDto;
import com.dmdev.domain.dto.order.OrderCreateRequestDto;
import com.dmdev.domain.dto.order.OrderResponseDto;
import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.dto.user.request.UserUpdateRequestDto;
import com.dmdev.domain.dto.user.response.UserResponseDto;
import com.dmdev.domain.dto.userdetails.request.UserDetailsUpdateRequestDto;
import com.dmdev.domain.dto.userdetails.response.UserDetailsResponseDto;
import com.dmdev.domain.model.Color;
import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.Role;
import com.dmdev.domain.model.Transmission;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class TestDtoBuilder {


    public static UserCreateRequestDto createUserCreateRequestDTO() {
        return new UserCreateRequestDto(
                "test@gmail.com",
                "test",
                "Testtesttest1",
                "petia",
                "petrov",
                "Minsk",
                "+37529111-11-11",
                LocalDate.of(2000, 10, 10),
                "AG67482",
                LocalDate.of(2020, 10, 10),
                LocalDate.of(2030, 10, 10));
    }

    public static BrandCreateEditRequestDto createBrandCreateEditRequestDto() {
        return new BrandCreateEditRequestDto("toyota");
    }

    public static CarCreateRequestDto createCarRequestDto(Long brandId, Long modelId) {
        return new CarCreateRequestDto(brandId, modelId, 1L,
                Color.BLACK, 2022, "2020-6", "hsjdhfjs2789",
                false, null);
    }

    public static ModelCreateRequestDto createModelRequestDto(Long brandId) {
        return new ModelCreateRequestDto(brandId, "a10",
                Transmission.AUTOMATIC, EngineType.DIESEL);
    }

    public static CategoryCreateEditRequestDto createCategoryCreateEditRequestDto() {
        return new CategoryCreateEditRequestDto("super-econome", BigDecimal.valueOf(120L));
    }

    public static OrderCreateRequestDto createOrderRequestDto(Long userId, Long carId) {
        return new OrderCreateRequestDto(userId, carId, "passport", true, LocalDateTime.now(), LocalDateTime.now().plusDays(3));
    }


    public static UserCreateRequestDto createUserCreateRequestDTOWithExistsEmail() {
        return new UserCreateRequestDto(
                "admin@gmail.com",
                "test",
                "test",
                "petia",
                "petrov",
                "Minsk",
                "+37529111-11-11",
                LocalDate.of(2000, 10, 10),
                "AG67482",
                LocalDate.of(2020, 10, 10),
                LocalDate.of(2030, 10, 10));
    }


    public static UserResponseDto getUserResponseDto() {
        return UserResponseDto.builder()
                .email("test@gmal.com")
                .username("test")
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

    public static UserDetailsUpdateRequestDto createUserDetailsUpdateRequestDTO() {
        return new UserDetailsUpdateRequestDto(
                "test",
                "test",
                "Minsk",
                "+37511111111");
    }

    public static DriverLicenseUpdateRequestDto createDriverLicenseUpdateRequestDTO() {
        return new DriverLicenseUpdateRequestDto(
                "aaa",
                LocalDate.now(),
                LocalDate.now().plusYears(1));
    }

    public static BrandCreateEditRequestDto createBrandUpdateRequestDTO() {
        return new BrandCreateEditRequestDto(
                "audis");
    }

    public static CategoryCreateEditRequestDto createCategoryUpdateRequestDto() {
        return new CategoryCreateEditRequestDto(
                "super-econome",
                BigDecimal.valueOf(150L));
    }

    public static CarUpdateRequestDto createCarUpdateRequestDTO(Long modelId) {
        return new CarUpdateRequestDto(modelId, 1L,
                Color.RED, 2019,
                "AT653-7", false, null);
    }

    public static ModelUpdateRequestDto createModelUpdateRequestDTO() {
        return new ModelUpdateRequestDto("A7", Transmission.AUTOMATIC, EngineType.DIESEL);
    }
}