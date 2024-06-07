package com.studyjun.lotto.dto.member.response;

import com.studyjun.lotto.entitiy.Member;

public record MemberUpdateResponse(
//        @Schema(description = "회원 정보 수정 성공 여부", example = "true")
        boolean result,
//        @Schema(description = "회원 이름", example = "윤준혁")
        String nickname,
//        @Schema(description = "생년월일", example = "1996-06-04")
        String birth,
//        @Schema(description = "이메일", example = "email@email.com")
        String email,
//        @Schema(description = "핸드폰 번호", example = "010-0101-0101")
        String phoneNumber
) {
    public static MemberUpdateResponse of(boolean result, Member member) {
        return new MemberUpdateResponse(
                result,
                member.getNickname(),
                member.getBirth(),
                member.getEmail(),
                member.getPhoneNumber()
        );
    }
}