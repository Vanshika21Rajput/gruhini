package com.example.Gruhani;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GruhaniApplication {

	public static void main(String[] args) {
		SpringApplication.run(GruhaniApplication.class, args);
	}

}
