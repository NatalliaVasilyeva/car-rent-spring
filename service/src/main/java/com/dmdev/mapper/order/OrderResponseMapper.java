package com.dmdev.mapper.order;

import com.dmdev.domain.dto.order.OrderResponseDto;
import com.dmdev.domain.entity.Order;
import com.dmdev.mapper.ResponseMapper;
import com.dmdev.mapper.car.CarResponseMapper;
import com.dmdev.mapper.user.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderResponseMapper implements ResponseMapper<Order, OrderResponseDto> {

    private final UserResponseMapper userResponseMapper;
    private final CarResponseMapper carResponseMapper;

    @Override
    public OrderResponseDto mapToDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .date(order.getDate())
                .startRentalDate(order.getCarRentalTime().getStartRentalDate())
                .endRentalDate(order.getCarRentalTime().getEndRentalDate())
                .user(userResponseMapper.mapToDto(order.getUser()))
                .car(carResponseMapper.mapToDto(order.getCar()))
                .insurance(order.isInsurance())
                .orderStatus(order.getOrderStatus())
                .sum(order.getSum())
                .build();
    }
}