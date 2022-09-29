package integration.com.dmdev.utils.builder;

import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.entity.Order;
import com.dmdev.domain.entity.Price;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.domain.model.Color;
import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.OrderStatus;
import com.dmdev.domain.model.Role;
import com.dmdev.domain.model.Transmission;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class ExistTesEntityBuilder {

    public static Accident getExistAccident() {
        return Accident.builder()
                .id(2L)
                .orderId(2L)
                .accidentDate(LocalDate.of(2022, 9, 03))
                .description("accident")
                .damage(BigDecimal.valueOf(10.05))
                .build();
    }

    public static Accident getUpdatedAccident() {
        return Accident.builder()
                .id(2L)
                .orderId(2L)
                .accidentDate(LocalDate.of(2022, 9, 22))
                .description("updated accident")
                .damage(BigDecimal.valueOf(10.05))
                .build();
    }

    public static Brand getExistBrand() {
        return Brand.builder()
                .id(2L)
                .name("mercedes")
                .build();
    }

    public static Brand getUpdatedBrand() {
        return Brand.builder()
                .id(2L)
                .name("audidos")
                .build();
    }

    public static Car getExistCar() {
        return Car.builder()
                .id(2L)
                .modelId(2l)
                .color(Color.RED)
                .year("2022")
                .carNumber("7834AE-7")
                .vin("AmhBdhjJ8BgD0p3PRgkoi")
                .isRepaired(false)
                .build();
    }

    public static Car getUpdatedCar() {
        return Car.builder()
                .id(2l)
                .modelId(2l)
                .color(Color.WHITE)
                .year("2022")
                .carNumber("7834AE-7")
                .vin("AmhBdhjJ8BgD0p3PRgkoi")
                .isRepaired(true)
                .build();
    }

    public static CarRentalTime getExistCarRentalTime() {
        return CarRentalTime.builder()
                .id(2L)
                .orderId(2l)
                .startRentalDate(LocalDateTime.of(2022, 9, 02, 00, 00))
                .endRentalDate(LocalDateTime.of(2022, 9, 04, 23, 59))
                .build();
    }

    public static CarRentalTime getUpdatedCarRentalTime() {
        return CarRentalTime.builder()
                .id(2L)
                .orderId(2l)
                .startRentalDate(LocalDateTime.of(2022, 10, 02, 00, 00))
                .endRentalDate(LocalDateTime.of(2022, 10, 04, 23, 59))
                .build();
    }

    public static Category getExistCategory() {
        return Category.builder()
                .id(2L)
                .name("BUSINESS")
                .priceId(2l)
                .build();
    }

    public static Category getUpdatedCategory() {
        return Category.builder()
                .id(2L)
                .name("SPORT")
                .priceId(2l)
                .build();
    }

    public static DriverLicense getExistDriverLicense() {
        return DriverLicense.builder()
                .id(2L)
                .userDetailsId(2L)
                .number("AB12346")
                .issueDate(LocalDate.of(2014, 3, 02))
                .expiredDate(LocalDate.of(2024, 12, 01))
                .build();
    }

    public static DriverLicense getUpdatedDriverLicense() {
        return DriverLicense.builder()
                .id(2L)
                .userDetailsId(2L)
                .number("AB12346")
                .issueDate(LocalDate.of(2015, 3, 02))
                .expiredDate(LocalDate.of(2025, 12, 01))
                .build();
    }

    public static Model getExistModel() {
        return Model.builder()
                .id(2L)
                .brandId(2L)
                .categoryId(2L)
                .name("Benz")
                .transmission(Transmission.ROBOT)
                .engineType(EngineType.FUEL)
                .build();
    }

    public static Model getUpdatedModel() {
        return Model.builder()
                .id(2L)
                .brandId(2L)
                .categoryId(2L)
                .name("Benz")
                .transmission(Transmission.AUTOMATIC)
                .engineType(EngineType.DIESEL)
                .build();
    }

    public static Order getExistOrder() {
        return Order.builder()
                .id(2l)
                .date(LocalDate.of(2022, 7, 02))
                .userId(2L)
                .carId(2L)
                .passport("MP1234589")
                .insurance(true)
                .orderStatus(OrderStatus.PAYED)
                .sum(BigDecimal.valueOf(300.00).setScale(2))
                .build();
    }

    public static Order getUpdatedOrder() {
        return Order.builder()
                .id(2l)
                .date(LocalDate.of(2022, 7, 02))
                .userId(2L)
                .carId(2L)
                .passport("MP1234589")
                .insurance(true)
                .orderStatus(OrderStatus.PAYED)
                .sum(BigDecimal.valueOf(500))
                .build();
    }

    public static Price getExistPrice() {
        return Price.builder()
                .id(2L)
                .price(BigDecimal.valueOf(100))
                .build();
    }

    public static Price getUpdatedPrice() {
        return Price.builder()
                .id(2L)
                .price(BigDecimal.valueOf(110))
                .build();
    }

    public static User getExistUser() {
        return User.builder()
                .login("Client")
                .email("client@gmail.com")
                .password("VasilechekBel123!")
                .role(Role.CLIENT)
                .build();
    }

    public static User getUpdatedUser() {
        return User.builder()
                .id(2l)
                .login("ClientUpdated")
                .email("ClientUpdated@gmail.com")
                .password("VasilechekBel123!'")
                .role(Role.CLIENT)
                .build();
    }

    public static UserDetails getExistUserDetails() {
        return UserDetails.builder()
                .userId(2L)
                .name("Petia")
                .surname("Petrov")
                .address("Minsk")
                .phone("+375 29 124 56 79")
                .birthday(LocalDate.of(1989, 3, 12))
                .registrationDate(LocalDate.of(2022, 9, 22))
                .build();
    }

    public static UserDetails getUpdatedUserDetails() {
        return UserDetails.builder()
                .id(2L)
                .userId(2L)
                .name("Petia")
                .surname("Petrov")
                .address("Gomel")
                .phone("+375 29 124 56 79")
                .birthday(LocalDate.of(1989, 3, 12))
                .registrationDate(LocalDate.of(2022, 9, 22))
                .build();
    }
}