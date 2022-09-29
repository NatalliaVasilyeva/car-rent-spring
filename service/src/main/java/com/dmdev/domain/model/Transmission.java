package com.dmdev.domain.model;

import java.util.Arrays;
import java.util.Optional;

public enum Transmission {
    MANUAL,
    AUTOMATIC,
    ROBOT;

    public static Optional<Transmission> find(String transmission) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(transmission))
                .findFirst();
    }
}