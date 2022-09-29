package com.dmdev.domain.model;

import java.util.Arrays;
import java.util.Optional;

public enum EngineType {
    FUEL,
    ELECTRIC,
    GAS,
    DIESEL;

    public static Optional<EngineType> find(String engineType) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(engineType))
                .findFirst();
    }
}