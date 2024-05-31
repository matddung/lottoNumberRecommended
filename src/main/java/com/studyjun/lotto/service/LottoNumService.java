package com.studyjun.lotto.service;

import com.studyjun.lotto.dto.lottoNum.response.LottoNumFrequency7Response;
import com.studyjun.lotto.dto.lottoNum.response.LottoNumFrequencyResponse;
import com.studyjun.lotto.dto.lottoNum.response.RandomNumResponse;
import com.studyjun.lotto.entitiy.LottoNum;
import com.studyjun.lotto.repository.LottoNumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

;

@RequiredArgsConstructor
@Service
@Transactional
public class LottoNumService {
    private final LottoNumRepository lottoNumRepository;

    @Transactional(readOnly = true)
    public LottoNumFrequencyResponse calculateWinningNumbersFrequency() {
        List<LottoNum> lottos = lottoNumRepository.findAll();
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (int i = 1; i <= 45; i++) {
            frequencyMap.put(i, 0);
        }

        for (LottoNum lotto : lottos) {
            incrementFrequency(frequencyMap, lotto.getNum1());
            incrementFrequency(frequencyMap, lotto.getNum2());
            incrementFrequency(frequencyMap, lotto.getNum3());
            incrementFrequency(frequencyMap, lotto.getNum4());
            incrementFrequency(frequencyMap, lotto.getNum5());
            incrementFrequency(frequencyMap, lotto.getNum6());
            incrementFrequency(frequencyMap, lotto.getBonusNum());
        }

        return new LottoNumFrequencyResponse(frequencyMap);
    }

    private void incrementFrequency(Map<Integer, Integer> map, int number) {
        map.put(number, map.get(number) + 1);
    }

    public LottoNumFrequency7Response findTop7FrequentNumbers() {
        LottoNumFrequencyResponse lottoNumFrequencyResponse = calculateWinningNumbersFrequency();
        Map<Integer, Integer> frequencyMap = lottoNumFrequencyResponse.map();

        List<Integer> topFrequentNumbers = frequencyMap.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(6) // 상위 6개만 선택
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return new LottoNumFrequency7Response(topFrequentNumbers);
    }

    public LottoNumFrequency7Response findBottom7FrequentNumbers() {
        LottoNumFrequencyResponse lottoNumFrequencyResponse = calculateWinningNumbersFrequency();
        Map<Integer, Integer> frequencyMap = lottoNumFrequencyResponse.map();

        List<Integer> bottomFrequentNumbers = frequencyMap.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue())
                .limit(6)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return new LottoNumFrequency7Response(bottomFrequentNumbers);
    }

    public RandomNumResponse randomNumbers() {
        Random random = new Random();
        Set<Integer> set = new LinkedHashSet<>();

        while (set.size() < 6) {
            set.add(random.nextInt(44) + 1);
        }

        List<Integer> list = new ArrayList<>(set);
        Collections.sort(list);

        return new RandomNumResponse(list);
    }
}