package com.soulmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing
public class SoulmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoulmateApplication.class, args);
	}

}
