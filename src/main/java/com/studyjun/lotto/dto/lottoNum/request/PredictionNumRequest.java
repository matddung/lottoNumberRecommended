package com.studyjun.lotto.dto.lottoNum.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record PredictionNumRequest(
        @Schema(description = "예측 날짜 입력", example = "2024-06-01")
        String inputDate
) {
}
