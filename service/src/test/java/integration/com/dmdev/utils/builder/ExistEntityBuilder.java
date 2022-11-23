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
import integration.com.dmdev.utils.TestEntityIdConst;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class ExistEntityBuilder {

    public static Accident getExistAccident() {
        return Accident.builder()
                .id(TestEntityIdConst.TEST_EXISTS_ACCIDENT_ID)
                .order(getExistOrder())
                .accidentDate(LocalDate.of(2022, 9, 3))
                .description("accident")
                .damage(BigDecimal.valueOf(10.05))
                .build();
    }

    public static Brand getExistBrand() {
        return Brand.builder()
                .id(TestEntityIdConst.TEST_EXISTS_BRAND_ID)
                .name("mercedes")
                .build();
    }

    public static Car getExistCar() {
        return Car.builder()
                .id(TestEntityIdConst.TEST_EXISTS_CAR_ID)
                .brand(getExistBrand())
                .model(getExistModel())
                .category(getExistCategory())
                .color(Color.RED)
                .year(2022)
                .carNumber("7834AE-7")
                .vin("AmhBdhjJ8BgD0p3PRgkoi")
                .repaired(false)
                .build();
    }

    public static CarRentalTime getExistCarRentalTime() {
        return CarRentalTime.builder()
                .id(TestEntityIdConst.TEST_EXISTS_CAR_RENTAL_TIME_ID)
                .order(getExistOrder())
                .startRentalDate(LocalDateTime.of(2022, 9, 2, 0, 0))
                .endRentalDate(LocalDateTime.of(2022, 9, 4, 23, 59))
                .build();
    }

    public static Category getExistCategory() {
        return Category.builder()
                .id(TestEntityIdConst.TEST_EXISTS_CATEGORY_ID)
                .name("BUSINESS")
                .price(BigDecimal.valueOf(100))
                .build();
    }

    public static DriverLicense getExistDriverLicense() {
        return DriverLicense.builder()
                .id(TestEntityIdConst.TEST_EXISTS_DRIVER_LICENSE_ID)
                .userDetails(getExistUserDetails())
                .number("AB12346")
                .issueDate(LocalDate.of(2014, 3, 2))
                .expiredDate(LocalDate.of(2024, 12, 1))
                .build();
    }

    public static Model getExistModel() {
        return Model.builder()
                .id(TestEntityIdConst.TEST_EXISTS_MODEL_ID)
                .brand(getExistBrand())
                .name("Benz")
                .transmission(Transmission.ROBOT)
                .engineType(EngineType.FUEL)
                .build();
    }

    public static Order getExistOrder() {
        return Order.builder()
                .id(TestEntityIdConst.TEST_EXISTS_ORDER_ID)
                .date(LocalDate.of(2022, 7, 2))
                .user(getExistUser())
                .car(getExistCar())
                .passport("MP1234589")
                .insurance(true)
                .orderStatus(OrderStatus.PAYED)
                .sum(BigDecimal.valueOf(300.00).setScale(2, RoundingMode.UNNECESSARY))
                .build();
    }

    public static User getExistUser() {
        return User.builder()
                .id(TestEntityIdConst.TEST_EXISTS_USER_ID)
                .username("Client")
                .email("client@gmail.com")
                .password("VasilechekBel123!")
                .role(Role.CLIENT)
                .build();
    }

    public static UserDetails getExistUserDetails() {
        return UserDetails.builder()
                .id(TestEntityIdConst.TEST_EXISTS_USER_DETAILS_ID)
                .user(getExistUser())
                .name("Petia")
                .surname("Petrov")
                .userContact(UserContact.builder()
                        .address("Minsk")
                        .phone("+375 29 124 56 79")
                        .build())
                .birthday(LocalDate.of(1989, 3, 12))
                .registrationDate(LocalDate.of(2022, 9, 22))
                .build();
    }
}