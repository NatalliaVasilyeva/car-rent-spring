package com.dmdev.domain.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Optional;

public enum Role implements GrantedAuthority {
    CLIENT,
    ADMIN;

    public static Optional<Role> find(String role) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(role))
                .findFirst();
    }
    @Override
    public String getAuthority() {
        return name();
    }
}