package com.hellchang;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.Base64;

@SpringBootTest
class HellchangApplicationTests {

	@Test
	void contextLoads() {
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[32];
		random.nextBytes(bytes);
		String secret = Base64.getEncoder().encodeToString(bytes);
		System.out.println("secret = " + secret);
	}

}
