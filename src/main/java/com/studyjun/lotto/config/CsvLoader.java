package com.studyjun.lotto.config;

import com.studyjun.lotto.entitiy.LottoNum;
import com.studyjun.lotto.repository.LottoNumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class CsvLoader implements CommandLineRunner {
    @Autowired
    LottoNumRepository lottoNumRepository;

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("/lottoInfo.csv").getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            LottoNum lottoNum = new LottoNum();
            lottoNum.setRound(Integer.parseInt(data[0]));
            lottoNum.setNum1(Integer.parseInt(data[1]));
            lottoNum.setNum2(Integer.parseInt(data[2]));
            lottoNum.setNum3(Integer.parseInt(data[3]));
            lottoNum.setNum4(Integer.parseInt(data[4]));
            lottoNum.setNum5(Integer.parseInt(data[5]));
            lottoNum.setNum6(Integer.parseInt(data[6]));
            lottoNum.setBonusNum(Integer.parseInt(data[7]));
            lottoNum.setFirstPlaceNumber(Integer.parseInt(data[8]));
            lottoNumRepository.save(lottoNum);
        }
    }
}