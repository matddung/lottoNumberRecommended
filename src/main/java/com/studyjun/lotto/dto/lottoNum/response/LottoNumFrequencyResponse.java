package com.studyjun.lotto.dto.lottoNum.response;

import java.util.Map;

public record LottoNumFrequencyResponse(
        Map<Integer, Integer> map
) {
}