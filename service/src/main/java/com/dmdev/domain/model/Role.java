package com.dmdev.domain.model;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    CLIENT,
    ADMIN;

    public static Optional<Role> find(String role) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(role))
                .findFirst();
    }
}