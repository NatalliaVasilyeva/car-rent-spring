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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class ExistEntityBuilder {

    public static Accident getExistAccident() {
        return Accident.builder()
                .id(2L)
                .order(getExistOrder())
                .accidentDate(LocalDate.of(2022, 9, 3))
                .description("accident")
                .damage(BigDecimal.valueOf(10.05))
                .build();
    }

    public static Brand getExistBrand() {
        return Brand.builder()
                .id(2L)
                .name("mercedes")
                .build();
    }

    public static Car getExistCar() {
        return Car.builder()
                .id(2L)
                .model(getExistModel())
                .color(Color.RED)
                .year(2022)
                .carNumber("7834AE-7")
                .vin("AmhBdhjJ8BgD0p3PRgkoi")
                .isRepaired(false)
                .build();
    }

    public static CarRentalTime getExistCarRentalTime() {
        return CarRentalTime.builder()
                .id(2L)
                .order(getExistOrder())
                .startRentalDate(LocalDateTime.of(2022, 9, 2, 0, 0))
                .endRentalDate(LocalDateTime.of(2022, 9, 4, 23, 59))
                .build();
    }

    public static Category getExistCategory() {
        return Category.builder()
                .id(2L)
                .name("BUSINESS")
                .price(getExistPrice())
                .build();
    }

    public static DriverLicense getExistDriverLicense() {
        return DriverLicense.builder()
                .id(2L)
                .userDetails(getExistUserDetails())
                .number("AB12346")
                .issueDate(LocalDate.of(2014, 3, 2))
                .expiredDate(LocalDate.of(2024, 12, 1))
                .build();
    }

    public static Model getExistModel() {
        return Model.builder()
                .id(2L)
                .brand(getExistBrand())
                .category(getExistCategory())
                .name("Benz")
                .transmission(Transmission.ROBOT)
                .engineType(EngineType.FUEL)
                .build();
    }

    public static Order getExistOrder() {
        return Order.builder()
                .id(2L)
                .date(LocalDate.of(2022, 7, 2))
                .user(getExistUser())
                .car(getExistCar())
                .passport("MP1234589")
                .insurance(true)
                .orderStatus(OrderStatus.PAYED)
                .sum(BigDecimal.valueOf(300.00).setScale(2, RoundingMode.UNNECESSARY))
                .build();
    }

    public static Price getExistPrice() {
        return Price.builder()
                .id(2L)
                .sum(BigDecimal.valueOf(100))
                .build();
    }

    public static User getExistUser() {
        return User.builder()
                .id(2L)
                .login("Client")
                .email("client@gmail.com")
                .password("VasilechekBel123!")
                .role(Role.CLIENT)
                .build();
    }

    public static UserDetails getExistUserDetails() {
        return UserDetails.builder()
                .id(2L)
                .user(getExistUser())
                .name("Petia")
                .surname("Petrov")
                .address("Minsk")
                .phone("+375 29 124 56 79")
                .birthday(LocalDate.of(1989, 3, 12))
                .registrationDate(LocalDate.of(2022, 9, 22))
                .build();
    }
}