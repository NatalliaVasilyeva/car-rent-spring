package com.dmdev.mapper.carrentaltime;

import com.dmdev.domain.dto.carrentaltime.CarRentalTimeUpdateRequestDto;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
public class CarRentalTimeUpdateMapper implements UpdateMapper<CarRentalTimeUpdateRequestDto, CarRentalTime> {


    @Override
    public CarRentalTime mapToEntity(CarRentalTimeUpdateRequestDto requestDto, CarRentalTime existing) {
        merge(requestDto, existing);
        return existing;
    }

    @Override
    public void merge(CarRentalTimeUpdateRequestDto requestDto, CarRentalTime existing) {
        existing.setStartRentalDate(requestDto.getStartRentalDate());
        existing.setEndRentalDate(requestDto.getEndRentalDate());
    }
}