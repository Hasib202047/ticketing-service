package com.hasib.ticketing_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TicketingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketingServiceApplication.class, args);
	}

}
