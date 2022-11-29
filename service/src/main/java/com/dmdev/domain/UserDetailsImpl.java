package com.dmdev.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Setter
@Getter
public class UserDetailsImpl extends User implements CustomUserDetails {

    private Long id;

    private String user;

    public UserDetailsImpl(String email, String password, Collection<? extends GrantedAuthority> authorities, Long id, String user) {
        super(email, password, authorities);
        this.id = id;
        this.user = user;
    }
}