package com.studyjun.lotto.dto.lottoNum.response;

import java.util.List;

public record PredictNumbersNeuralNetworkResponse(
        boolean result,
        List<Integer> list
) {
}
