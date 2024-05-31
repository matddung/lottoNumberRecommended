package com.studyjun.lotto;

import com.studyjun.lotto.service.LottoNumService;
import com.studyjun.lotto.service.WekaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LottoApplicationTests {
	@Autowired
	private WekaService wekaService;

	@Autowired
	private LottoNumService lottoNumService;

	@Test
	void lottoNumServiceTest1() {
		System.out.println(lottoNumService.findTop7FrequentNumbers().list());
	}

	@Test
	void lottoNumServiceTest2() {
		System.out.println(lottoNumService.randomNumbers().list());
	}

	@Test
	void lottoNumServiceTest3() {
		System.out.println(lottoNumService.findBottom7FrequentNumbers().list());
	}

	@Test
	void wekaServiceTest1() throws Exception {
		System.out.println(wekaService.predictNumbersRegression("2024-06-01").list());
	}

	@Test
	void wekaServiceTest2() throws Exception {
		System.out.println(wekaService.predictNumbersNeuralNetwork("2024-06-01").list());
	}
}