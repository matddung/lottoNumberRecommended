package com.studyjun.lotto.oAuth2;

import com.studyjun.lotto.common.MemberType;
import com.studyjun.lotto.security.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2Member oAuth2User = (CustomOAuth2Member) authentication.getPrincipal();

            if(oAuth2User.getType() == MemberType.GUEST) {
                String accessToken = tokenProvider.createAccessToken(oAuth2User.getEmail());
                response.addHeader(tokenProvider.getAccessHeader(), "Bearer " + accessToken);
                response.sendRedirect("/swagger-ui/index.html"); // Todo : 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트

                tokenProvider.sendAccessAndRefreshToken(response, accessToken, null);
            } else {
                loginSuccess(response, oAuth2User);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2Member oAuth2User) throws IOException {
        String accessToken = tokenProvider.createAccessToken(oAuth2User.getEmail());
        String refreshToken = tokenProvider.createRefreshToken();
        response.addHeader(tokenProvider.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(tokenProvider.getRefreshHeader(), "Bearer " + refreshToken);

        tokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        tokenProvider.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }
}