package com.hellzzang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HellzzangApplication {

	public static void main(String[] args) {
		SpringApplication.run(HellzzangApplication.class, args);
	}

}
