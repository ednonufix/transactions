package org.example.transactions;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
class TransactionsApplicationTests {

	@Test
	void contextLoads() {
		log.info("Test context loads");
	}

}
