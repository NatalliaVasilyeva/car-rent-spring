package com.dmdev.mapper.carrentaltime;

import com.dmdev.domain.dto.order.OrderCreateRequestDto;
import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.UserContact;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.mapper.CreateMapper;
import org.springframework.stereotype.Component;

@Component
public class CarRentalTimeFromOrderCreateMapper implements CreateMapper<OrderCreateRequestDto, CarRentalTime> {

    @Override
    public CarRentalTime mapToEntity(OrderCreateRequestDto requestDto) {
        return CarRentalTime.builder()
                .startRentalDate(requestDto.getStartRentalDate())
                .endRentalDate(requestDto.getEndRentalDate())
                .build();
    }
}