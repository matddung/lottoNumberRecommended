package com.studyjun.lotto.security;

import com.studyjun.lotto.CustomOAuth2MemberService;
import com.studyjun.lotto.OAuth2LoginFailureHandler;
import com.studyjun.lotto.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint entryPoint;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2MemberService customOAuth2UserService;

    private static final String[] ALLOWED_URIS = {
            "/sign-up",
            "/sign-in",
            "/login/oauth2/**",
            "/oauth2/**",
            "/loginSuccess",
            "/loginFailure"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .headers(headers -> headers.frameOptions().sameOrigin())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(ALLOWED_URIS).permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                        .userInfoEndpoint().userService(customOAuth2UserService)
                )
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler oAuth2SuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler("/loginSuccess");
    }

    @Bean
    public AuthenticationFailureHandler oAuth2FailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/loginFailure");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}