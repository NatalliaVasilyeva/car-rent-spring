package com.dmdev.mapper.order;

import com.dmdev.domain.dto.order.OrderUpdateRequestDto;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.Order;
import com.dmdev.mapper.UpdateMapper;
import com.dmdev.repository.CarRentalTimeRepository;
import com.dmdev.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderUpdateMapper implements UpdateMapper<OrderUpdateRequestDto, Order> {

    private final CarRepository carRepository;
    private final CarRentalTimeRepository carRentalTimeRepository;

    @Override
    public Order mapToEntity(OrderUpdateRequestDto requestDto, Order existing) {
        merge(requestDto, existing);
        return existing;
    }

    @Override
    public void merge(OrderUpdateRequestDto requestDto, Order existing) {
        existing.setCar(getCar(requestDto.getCarId()));
        existing.setInsurance(requestDto.getInsurance());
        var carRentalTime = getCarRentalTime(existing.getCarRentalTime().getId());
        carRentalTime.setStartRentalDate(requestDto.getStartRentalDate());
        carRentalTime.setEndRentalDate(requestDto.getEndRentalDate());
        carRentalTime.setOrder(existing);
    }

    private Car getCar(Long carId) {
        return Optional.ofNullable(carId)
                .flatMap(carRepository::findById)
                .orElse(null);
    }

    private CarRentalTime getCarRentalTime(Long orderId) {
        return Optional.ofNullable(orderId)
                .flatMap(carRentalTimeRepository::findByOrderId)
                .orElse(null);
    }
}