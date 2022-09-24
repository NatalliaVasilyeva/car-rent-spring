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
public class FakeTestEntityBuilder {

    public static Accident createAccident() {
        return Accident.builder()
                .orderId(2L)
                .accidentDate(LocalDate.of(2022, 9, 22))
                .description("Updated accident with car")
                .damage(BigDecimal.valueOf(40))
                .build();
    }

    public static Brand createBrand() {
        return Brand.builder()
                .name("opel")
                .build();
    }

    public static Car createCar() {
        return Car.builder()
                .modelId(2l)
                .color(Color.WHITE)
                .year("2022")
                .carNumber("AT7654")
                .vin("hjsdhfBJHS84")
                .isRepaired(false)
                .build();
    }

    public static CarRentalTime createCarRentalTime() {
        return CarRentalTime.builder()
                .orderId(3l)
                .startRentalDate(LocalDateTime.of(2022, 8, 20, 10, 50))
                .endRentalDate(LocalDateTime.of(2022, 8, 25, 9, 59))
                .build();
    }

    public static Category createCategory() {
        return Category.builder()
                .name("'BUSINESS'")
                .priceId(2L)
                .build();
    }

    public static DriverLicense createDriverLicense() {
        return DriverLicense.builder()
                .userDetailsId(3l)
                .number("6878942")
                .issueDate(LocalDate.of(2019, 8, 17))
                .expiredDate(LocalDate.of(2029, 8, 16))
                .build();
    }

    public static Model createModel() {
        return Model.builder()
                .brandId(2l)
                .categoryId(2l)
                .name("M3")
                .engineType(EngineType.DIESEL)
                .transmission(Transmission.AUTOMATIC)
                .build();
    }

    public static Order createOrder() {
        return Order.builder()
                .date(LocalDate.of(2022, 9, 22))
                .userId(2L)
                .carId(2L)
                .passport("MP67236")
                .insurance(true)
                .orderStatus(OrderStatus.DECLINED)
                .sum(BigDecimal.valueOf(15))
                .build();
    }

    public static Price createPrice() {
        return Price.builder()
                .price(BigDecimal.valueOf(99))
                .build();
    }

    public static User createUser() {
        return User.builder()
                .login("newAdmin")
                .email("newAdmin@gmail.com")
                .password("newAdmin")
                .role(Role.ADMIN)
                .build();
    }

    public static UserDetails createUserDetails() {
        return UserDetails.builder()
                .userId(3L)
                .name("Nikolai")
                .surname("Ivanov")
                .address("Minsk")
                .phone("+375 29 126 54 79")
                .birthday(LocalDate.of(2000, 1, 1))
                .registrationDate(LocalDate.of(2022, 9, 22))
                .build();
    }

}