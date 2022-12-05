package com.dmdev.mapper.order;

import com.dmdev.domain.dto.order.OrderCreateRequestDto;
import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.Order;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.model.OrderStatus;
import com.dmdev.mapper.Mapper;
import com.dmdev.mapper.carrentaltime.CarRentalTimeFromOrderCreateMapper;
import com.dmdev.mapper.user.DriverLicenseFromUserCreateMapper;
import com.dmdev.mapper.user.UserDetailsFromUserCreateMapper;
import com.dmdev.repository.CarRepository;
import com.dmdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderCreateMapper implements Mapper<OrderCreateRequestDto, Order> {

    private final CarRentalTimeFromOrderCreateMapper carRentalTimeCreateMapper;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    public Order mapToEntity(OrderCreateRequestDto requestDto) {
        var user = getUser(requestDto.getUserId());
        var car = getCar(requestDto.getCarId());
        var carRentalTime = carRentalTimeCreateMapper.mapToEntity(requestDto);
        var order = Order.builder()
                .date(LocalDate.now())
                .passport(requestDto.getPassport())
                .insurance(requestDto.getInsurance())
                .orderStatus(OrderStatus.CONFIRMATION_WAIT)
                .build();

        order.setUser(user);
        order.setCar(car);
        carRentalTime.setOrder(order);
        return order;
    }

    private User getUser(Long userId) {
        return Optional.ofNullable(userId)
                .flatMap(userRepository::findById)
                .orElse(null);
    }

    private Car getCar(Long carId) {
       return Optional.ofNullable(carId)
                .flatMap(carRepository::findById)
                .orElse(null);
    }
}