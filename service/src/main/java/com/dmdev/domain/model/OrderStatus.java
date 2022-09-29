package com.dmdev.domain.model;

import java.util.Arrays;
import java.util.Optional;

public enum OrderStatus {
    CONFIRMATION_WAIT,
    DECLINED,
    PAYED,
    NOT_PAYED,
    CANCELLED;

    public static Optional<OrderStatus> find(String status) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(status))
                .findFirst();
    }
}