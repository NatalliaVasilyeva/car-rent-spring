package com.dmdev.mapper.accident;

import com.dmdev.domain.dto.accident.AccidentResponseDto;
import com.dmdev.domain.entity.Accident;
import com.dmdev.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class AccidentResponseMapper implements ResponseMapper<Accident, AccidentResponseDto> {

    public AccidentResponseDto mapToDto(Accident accident) {
        return AccidentResponseDto.builder()
                .id(accident.getId())
                .date(accident.getAccidentDate())
                .orderNumber(accident.getOrder().getId())
                .brand(accident.getOrder().getCar().getBrand().getName())
                .model(accident.getOrder().getCar().getModel().getName())
                .carNumber(accident.getOrder().getCar().getCarNumber())
                .userFirstName(accident.getOrder().getUser().getUserDetails().getName())
                .userLastName(accident.getOrder().getUser().getUserDetails().getSurname())
                .description(accident.getDescription())
                .damage(accident.getDamage())
                .build();
    }
}