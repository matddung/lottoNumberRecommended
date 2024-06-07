package com.studyjun.lotto.dto.email.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindPasswordRequest(
        @Schema(description = "이메일", example = "matddung76@naver.com")
        String email,
        @Schema(description = "회원 아이디", example = "test1")
        String account
) {
}