package com.studyjun.lotto.controller;

import com.studyjun.lotto.dto.ApiResponse;
import com.studyjun.lotto.dto.lottoNum.request.PredictionNumRequest;
import com.studyjun.lotto.dto.member.request.MemberUpdateRequest;
import com.studyjun.lotto.security.UserAuthorize;
import com.studyjun.lotto.service.LottoNumService;
import com.studyjun.lotto.service.MemberService;
import com.studyjun.lotto.service.WekaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "멤버 API")
@RequiredArgsConstructor
@UserAuthorize
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final LottoNumService lottoNumService;
    private final WekaService wekaService;

    @Operation(summary = "회원 정보 조회")
    @GetMapping
    public ApiResponse getMemberInfo(@AuthenticationPrincipal User user) {
        return ApiResponse.success(memberService.getMemberInfo(UUID.fromString(user.getUsername())));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public ApiResponse deleteMember(@AuthenticationPrincipal User user) {
        return ApiResponse.success(memberService.deleteMember(UUID.fromString(user.getUsername())));
    }

    @Operation(summary = "회원 정보 수정")
    @PatchMapping
    public ApiResponse updateMember(@AuthenticationPrincipal User user, @RequestBody MemberUpdateRequest request) {
        return ApiResponse.success(memberService.updateMember(UUID.fromString(user.getUsername()), request));
    }

    @Operation(summary = "로또 번호 빈도 보기")
    @GetMapping("/frequencyNumber")
    public ApiResponse getFrequencyNumber() {
        return ApiResponse.success(lottoNumService.calculateWinningNumbersFrequency());
    }

    @Operation(summary = "로또 번호 빈도 상위 7개")
    @GetMapping("/frequencyNumber/top7")
    public ApiResponse getFrequencyNumberTop7() {
        return ApiResponse.success(lottoNumService.findTop7FrequentNumbers());
    }

    @Operation(summary = "로또 번호 빈도 하위 7개")
    @GetMapping("/frequencyNumber/bottom7")
    public ApiResponse getFrequencyNumberBottom7() {
        return ApiResponse.success(lottoNumService.findBottom7FrequentNumbers());
    }

    @Operation(summary = "로또 번호 랜덤")
    @GetMapping("/randomNumber")
    public ApiResponse randomNumber6() {
        return ApiResponse.success(lottoNumService.randomNumbers());
    }

    @Operation(summary = "인공 신경망 번호")
    @PatchMapping("/neuralNumbers")
    public ApiResponse neuralNumbers(@RequestBody PredictionNumRequest request) throws Exception{
        return ApiResponse.success(wekaService.predictNumbersNeuralNetwork(request.inputDate()));
    }

    @Operation(summary = "다중 회귀 번호")
    @PatchMapping("/regressionNumbers")
    public ApiResponse regressionNumbers(@RequestBody PredictionNumRequest request) throws Exception{
        return ApiResponse.success(wekaService.predictNumbersRegression(request.inputDate()));
    }
}