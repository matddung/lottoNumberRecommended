package com.studyjun.lotto.controller;

import com.studyjun.lotto.service.CustomOAuth2MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OAuth2Controller {
    private final AuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler("/loginSuccess");
    private final AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler("/loginFailure");
    private final CustomOAuth2MemberService customOAuth2MemberService;

    @GetMapping("/oauth2/authorization/facebook")
    public void facebookLogin() {
        // facebook 로그인 처리
    }

    @GetMapping("/oauth2/authorization/google")
    public void googleLogin() {
        // google 로그인 처리
    }

    @GetMapping("/oauth2/authorization/naver")
    public void naverLogin() {
        // naver 로그인 처리
    }

    @GetMapping("/oauth2/authorization/kakao")
    public void kakaoLogin() {
        // kakao 로그인 처리
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(Authentication authentication) {
        // 로그인 성공 처리
        return "loginSuccess";
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        // 로그인 실패 처리
        return "loginFailure";
    }
}