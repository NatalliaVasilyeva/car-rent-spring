package com.dmdev.mapper.car;

import com.dmdev.domain.dto.car.CarResponseDto;
import com.dmdev.domain.entity.Car;
import com.dmdev.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class CarResponseMapper implements ResponseMapper<Car, CarResponseDto> {

    @Override
    public CarResponseDto mapToDto(Car car) {
        return CarResponseDto.builder()
                .id(car.getId())
                .brand(car.getBrand().getName())
                .model(car.getModel().getName())
                .color(car.getColor())
                .transmission(car.getModel().getTransmission())
                .engineType(car.getModel().getEngineType())
                .yearOfProduction(car.getYear())
                .number(car.getCarNumber())
                .vin(car.getVin())
                .repaired(car.isRepaired())
                .image(car.getImage())
                .category(car.getCategory().getName())
                .price(car.getCategory().getPrice())
                .build();
    }
}