package com.studyjun.lotto.controller;

import com.studyjun.lotto.dto.ApiResponse;
import com.studyjun.lotto.dto.email.request.FindPasswordRequest;
import com.studyjun.lotto.dto.signIn.request.SignInRequest;
import com.studyjun.lotto.dto.signUp.request.SignUpRequest;
import com.studyjun.lotto.service.CommonService;
import com.studyjun.lotto.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@Tag(name = "공용 API")
@RequiredArgsConstructor
@RestController
@RequestMapping
public class CommonController {
    private final CommonService commonService;
    private final EmailService emailService;

    @Operation(summary = "회원 가입")
    @PostMapping("/signUp")
    public ApiResponse signUp(@RequestBody SignUpRequest request) {
        return ApiResponse.success(commonService.signUp(request));
    }

    @Operation(summary = "로그인")
    @PostMapping("/signIn")
    public ApiResponse signIn(@RequestBody SignInRequest request) {
        return ApiResponse.success(commonService.signIn(request));
    }

    @Operation(summary = "임시 비밀번호 메일 보내기")
    @PostMapping("/findPassword")
    public ApiResponse findPassword(@RequestBody FindPasswordRequest request) throws MessagingException, UnsupportedEncodingException {
        return ApiResponse.success(emailService.sendTempPasswordMail(request));
    }
}