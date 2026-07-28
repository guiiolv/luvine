package com.luvine.modules.user.infrastructure.security;

import com.luvine.modules.user.domain.entity.UserCredentials;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserCredentialsDetails implements UserDetails {

    private final UserCredentials credentials;

    public UserCredentialsDetails(UserCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public @NullMarked Collection<? extends GrantedAuthority> getAuthorities() {
        return credentials.getRole() == null
                ? List.of()
                : credentials.getRole().getAuthorities()
                .stream()
                .map(auth -> new SimpleGrantedAuthority(auth.name()))
                .toList();
    }

    @Override
    public @Nullable String getPassword() {
        return credentials.getHashedPassword().value();
    }

    @Override
    public @NullMarked String getUsername() {
        return credentials.getEmail().value();
    }

    @Override
    public boolean isEnabled() {
        return credentials.isActive();
    }
}