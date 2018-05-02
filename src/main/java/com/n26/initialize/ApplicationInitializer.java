package com.n26.initialize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.n26.controller.ApplicationController;

@SpringBootApplication
public class ApplicationInitializer {

	public static void main(String[] args) {
		
		SpringApplication.run(ApplicationController.class, args);

	}

}
