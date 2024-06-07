package com.studyjun.lotto.dto.signUp.response;

import com.studyjun.lotto.entitiy.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record SignUpResponse(
        @Schema(description = "회원 고유키", example = "c0a80121-7aeb-4b4b-8b7a-9b9b9b9b9b9b")
        UUID id,
        @Schema(description = "회원 아이디", example = "studyjun")
        String account,
        @Schema(description = "회원 이름", example = "윤준혁")
        String nickname,
        @Schema(description = "생년월일", example = "1996-06-04")
        String birth,
        @Schema(description = "이메일", example = "email@email.com")
        String email,
        @Schema(description = "핸드폰 번호", example = "010-0101-0101")
        String phoneNumber
) {
    public static SignUpResponse from(Member member) {
        return new SignUpResponse(
                member.getId(),
                member.getAccount(),
                member.getNickname(),
                member.getBirth(),
                member.getEmail(),
                member.getPhoneNumber()
        );
    }
}