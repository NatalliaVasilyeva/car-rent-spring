package com.dmdev.domain;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {

    Long getId();

    String getUser();
}