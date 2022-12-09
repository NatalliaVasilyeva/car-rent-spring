package com.dmdev.mapper.order;

import com.dmdev.domain.dto.order.OrderUserReportDto;
import com.dmdev.domain.entity.Order;
import com.dmdev.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderReportMapper implements ResponseMapper<Order, OrderUserReportDto> {

    public OrderUserReportDto mapToDto(Order order) {
        return OrderUserReportDto.builder()
                .id(order.getId())
                .date(order.getDate())
                .startRentalDate(order.getCarRentalTime().getStartRentalDate())
                .endRentalDate(order.getCarRentalTime().getEndRentalDate())
                .brand(order.getCar().getBrand().getName())
                .model(order.getCar().getModel().getName())
                .color(order.getCar().getColor())
                .transmission(order.getCar().getModel().getTransmission())
                .engineType(order.getCar().getModel().getEngineType())
                .yearOfProduction(order.getCar().getYear())
                .insurance(order.isInsurance())
                .orderStatus(order.getOrderStatus())
                .sum(order.getSum())
                .build();
    }
}