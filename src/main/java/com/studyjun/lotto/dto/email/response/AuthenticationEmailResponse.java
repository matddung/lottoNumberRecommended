package com.studyjun.lotto.dto.email.response;

public record AuthenticationEmailResponse(
        boolean result,
        String authenticationMessage,
        String message
) {
}
