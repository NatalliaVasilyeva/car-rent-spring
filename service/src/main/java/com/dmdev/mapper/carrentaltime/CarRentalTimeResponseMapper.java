package com.dmdev.mapper.carrentaltime;

import com.dmdev.domain.dto.carrentaltime.CarRentalTimeResponseDto;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CarRentalTimeResponseMapper implements ResponseMapper<CarRentalTime, CarRentalTimeResponseDto> {


    @Override
    public CarRentalTimeResponseDto mapToDto(CarRentalTime carRentalTime) {
        return CarRentalTimeResponseDto.builder()
                .id(carRentalTime.getId())
                .orderId(carRentalTime.getOrder().getId())
                .startRentalDate(carRentalTime.getStartRentalDate())
                .endRentalDate(carRentalTime.getEndRentalDate())
                .build();
    }
}