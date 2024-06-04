package com.studyjun.lotto.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberType {
    GUEST("ROLE_GUEST"), USER("ROLE_USER");

    private final String key;
}
