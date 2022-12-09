package com.dmdev.mapper.accident;

import com.dmdev.domain.dto.accident.AccidentUpdateRequestDto;
import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Order;
import com.dmdev.mapper.UpdateMapper;
import com.dmdev.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccidentUpdateMapper implements UpdateMapper<AccidentUpdateRequestDto, Accident> {

    private final OrderRepository orderRepository;

    @Override
    public Accident mapToEntity(AccidentUpdateRequestDto requestDto, Accident existing) {
        merge(requestDto, existing);
        return existing;
    }

    @Override
    public void merge(AccidentUpdateRequestDto requestDto, Accident existing) {
        existing.setDescription(requestDto.getDescription());
        existing.setDamage(requestDto.getDamage());
        var order = getOrder(existing.getOrder().getId());
        order.setAccident(existing);
    }

    private Order getOrder(Long orderId) {
        return Optional.ofNullable(orderId)
                .flatMap(orderRepository::findById)
                .orElse(null);
    }

}