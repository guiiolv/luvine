package com.luvine.modules.user.domain.valueobject;

import lombok.Getter;

import java.util.List;

@Getter
public enum Role {
    CUSTOMER(List.of(Authority.ROLE_CUSTOMER)),
    ADMIN(List.of(Authority.ROLE_ADMIN, Authority.ROLE_CUSTOMER));

    private final List<Authority> authorities;

    Role(List<Authority> authorities) {
        this.authorities = authorities;
    }
}