package com.project.evm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class EvmApplication {
	public static void main(String[] args) {
		SpringApplication.run(EvmApplication.class, args);
	}
}
