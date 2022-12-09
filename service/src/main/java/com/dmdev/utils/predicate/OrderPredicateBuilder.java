package com.dmdev.utils.predicate;

import com.dmdev.domain.dto.filterdto.OrderFilter;
import com.dmdev.domain.model.OrderStatus;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.dmdev.domain.entity.QOrder.order;

@Component
public class OrderPredicateBuilder implements PredicateBuilder<Predicate, OrderFilter> {

    @Override
    public Predicate build(OrderFilter requestFilter) {
        return QPredicate.builder()
                .add(requestFilter.getUserFirstName(), order.user.userDetails.name::equalsIgnoreCase)
                .add(requestFilter.getUserLastName(), order.user.userDetails.surname::equalsIgnoreCase)
                .add(requestFilter.getCarNumber(), order.car.carNumber::equalsIgnoreCase)
                .add(requestFilter.getOrderStatus(), order.orderStatus::eq)
                .add(requestFilter.getSum(), order.sum::goe)
                .buildAnd();
    }

    public Predicate usersBuild(Long userId, OrderStatus status, BigDecimal sum) {
        return QPredicate.builder()
                .add(userId, order.user.id::eq)
                .add(status, order.orderStatus::eq)
                .add(sum, order.sum::goe)
                .buildAnd();
    }
}