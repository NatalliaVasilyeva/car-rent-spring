package integration.com.dmdev.utils.builder;

import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.entity.Order;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserContact;
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
public class TestEntityBuilder {

    public static Brand createBrand() {
        return Brand.builder()
                .name("opel")
                .build();
    }

    public static Car createCar() {
        return Car.builder()
                .color(Color.WHITE)
                .year(2022)
                .carNumber("AT7654")
                .vin("hjsdhfBJHS84")
                .repaired(false)
                .build();
    }

    public static Category createCategory() {
        return Category.builder()
                .name("'BUSINESS'")
                .price(BigDecimal.valueOf(99))
                .build();
    }

    public static DriverLicense createDriverLicense() {
        return DriverLicense.builder()
                .number("6878942")
                .issueDate(LocalDate.of(2019, 8, 17))
                .expiredDate(LocalDate.of(2029, 8, 16))
                .build();
    }

    public static Model createModel() {
        return Model.builder()
                .category(ExistEntityBuilder.getExistCategory())
                .name("M3")
                .engineType(EngineType.DIESEL)
                .transmission(Transmission.AUTOMATIC)
                .build();
    }

    public static Order createOrder() {
        return Order.builder()
                .date(LocalDate.of(2022, 9, 22))
                .passport("MP67236")
                .insurance(true)
                .orderStatus(OrderStatus.DECLINED)
                .sum(BigDecimal.valueOf(15))
                .build();
    }

    public static Accident createAccident() {
        return Accident.builder()
                .order(ExistEntityBuilder.getExistOrder())
                .damage(BigDecimal.valueOf(99.99))
                .description("accident test description saved")
                .accidentDate(LocalDate.of(2022, 9, 18))
                .build();
    }


    public static CarRentalTime createCarRentalTime() {
        return CarRentalTime.builder()
                .startRentalDate(LocalDateTime.of(2002, 10, 12, 11, 0))
                .endRentalDate(LocalDateTime.of(2002, 10, 13, 11, 0))
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
                .name("Nikolai")
                .surname("Ivanov")
                .userContact(UserContact.builder()
                        .address("Minsk")
                        .phone("+375 29 126 54 79")
                        .build())
                .birthday(LocalDate.of(2000, 1, 1))
                .registrationDate(LocalDate.of(2022, 9, 22))
                .build();
    }

    public static UserContact createUserContact() {
        return UserContact.builder()
                .address("Istanbul")
                .phone("+375 29 678 98 66")
                .build();
    }
}