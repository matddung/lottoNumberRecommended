package com.studyjun.lotto.dto.lottoNum.response;

import java.util.List;

public record PredictNumbersRegressionResponse(
        boolean result,
        List<Integer> list
) {
}