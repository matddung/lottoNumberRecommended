package com.studyjun.lotto.dto.email.response;

public record FindPasswordResponse(
        boolean result,
        String tempPassword,
        String message
) {
}