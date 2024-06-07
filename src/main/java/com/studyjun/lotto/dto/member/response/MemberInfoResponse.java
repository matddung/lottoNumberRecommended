package com.studyjun.lotto.dto.member.response;

import com.studyjun.lotto.common.MemberType;
import com.studyjun.lotto.entitiy.Member;

import java.util.UUID;

public record MemberInfoResponse(
//        @Schema(description = "회원 고유키", example = "c0a80121-7aeb-4b4b-8b0a-6b1c032f0e4a")
        UUID id,
//        @Schema(description = "회원 아이디", example = "test444")
        String account,
//        @Schema(description = "회원 이름", example = "윤준혁")
        String nickname,
//        @Schema(description = "생년월일", example = "1996-06-04")
        String birth,
//        @Schema(description = "회원 타입", example = "USER")
        MemberType type,
//        @Schema(description = "이메일", example = "email@email.com")
        String email,
//        @Schema(description = "핸드폰 번호", example = "010-0101-0101")
        String phoneNumber
) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getAccount(),
                member.getNickname(),
                member.getBirth(),
                member.getType(),
                member.getEmail(),
                member.getPhoneNumber()
        );
    }
}