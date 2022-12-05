package com.dmdev.mapper.carrentaltime;

import com.dmdev.domain.dto.carrentaltime.CarRentalTimeCreateRequestDto;
import com.dmdev.domain.dto.order.OrderCreateRequestDto;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.mapper.CreateMapper;
import org.springframework.stereotype.Component;

@Component
public class CarRentalTimeCreateMapper implements CreateMapper<CarRentalTimeCreateRequestDto, CarRentalTime> {

    @Override
    public CarRentalTime mapToEntity(CarRentalTimeCreateRequestDto requestDto) {
        return CarRentalTime.builder()
                .startRentalDate(requestDto.getStartRentalDate())
                .endRentalDate(requestDto.getEndRentalDate())
                .build();
    }
}