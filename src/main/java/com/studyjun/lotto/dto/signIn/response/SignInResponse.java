package com.studyjun.lotto.dto.signIn.response;

import com.studyjun.lotto.common.MemberType;

public record SignInResponse(
//        @Schema(description = "회원 이름", example = "윤준혁")
        String name,
//        @Schema(description = "회원 유형", example = "USER")
        MemberType type,
        String token,
        String refreshToken
) {
}