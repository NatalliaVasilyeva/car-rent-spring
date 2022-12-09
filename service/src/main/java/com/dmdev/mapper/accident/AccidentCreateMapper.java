package com.dmdev.mapper.accident;

import com.dmdev.domain.dto.accident.AccidentCreateRequestDto;
import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Order;
import com.dmdev.mapper.Mapper;
import com.dmdev.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccidentCreateMapper implements Mapper<AccidentCreateRequestDto, Accident> {

    private final OrderRepository orderRepository;

    public Accident mapToEntity(AccidentCreateRequestDto requestDto) {
        var order = getOrder(requestDto.getOrderId());
        var accident = Accident.builder()
                .accidentDate(requestDto.getAccidentDate())
                .description(requestDto.getDescription())
                .damage(requestDto.getDamage())
                .build();

        order.setAccident(accident);
        return accident;
    }

    private Order getOrder(Long orderId) {
        return Optional.ofNullable(orderId)
                .flatMap(orderRepository::findById)
                .orElse(null);
    }
}