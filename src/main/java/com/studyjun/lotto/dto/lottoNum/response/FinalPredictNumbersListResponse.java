package com.studyjun.lotto.dto.lottoNum.response;

import java.util.List;

public record FinalPredictNumbersListResponse (
        List<List<Integer>> list
) {
}
