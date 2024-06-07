package com.studyjun.lotto.dto.signUp.request;

public record SignUpRequest(
//        @Schema(description = "회원 아이디", example = "studyjun")
        String account,
//        @Schema(description = "회원 비밀번호", example = "1234")
        String password,
//        @Schema(description = "회원 이름", example = "윤준혁")
        String nickname,
//        @Schema(description = "생년월일", example = "1996-06-04")
        String birth,
//        @Schema(description = "이메일", example = "email@email.com")
        String email,
//        @Schema(description = "핸드폰 번호", example = "010-0101-0101")
        String phoneNumber
) {
}