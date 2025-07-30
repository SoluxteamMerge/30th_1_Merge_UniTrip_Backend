package com.Solux.UniTrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UniTripApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniTripApplication.class, args);
	}

}

