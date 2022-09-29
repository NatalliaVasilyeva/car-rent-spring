package com.dmdev.domain.model;

import java.util.Arrays;
import java.util.Optional;

public enum Color {
    RED,
    BLUE,
    WHITE,
    BLACK,
    GREEN,
    YELLOW;

    public static Optional<Color> find(String color) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(color))
                .findFirst();
    }
}